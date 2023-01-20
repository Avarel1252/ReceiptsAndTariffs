package com.receipts.ui.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.findNavController
import com.elveum.elementadapter.SimpleBindingAdapter
import com.receipts.databinding.FragmentReceiptsBinding
import com.receipts.ui.lists.database.DatabasesViewModel
import com.receipts.ui.lists.database.adapter.DatabasesAdapterListener
import com.receipts.ui.lists.database.adapter.databasesAdapter
import com.receipts.ui.lists.receipts.ReceiptListItem
import com.receipts.ui.lists.receipts.ReceiptsViewModel
import com.receipts.ui.lists.receipts.adapter.ReceiptsAdapterListener
import com.receipts.ui.lists.receipts.adapter.receiptsSimpleAdapter
import com.receipts.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.notify
import okio.Closeable

@AndroidEntryPoint
class ReceiptsListFragment : Fragment() {

    private lateinit var receiptsViewModel: ReceiptsViewModel
    private val dbsViewModel: DatabasesViewModel by activityViewModels()
    private lateinit var binding: FragmentReceiptsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReceiptsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiptsViewModel = viewModels<ReceiptsViewModel>().value
        with(binding) {
            rvDbs.adapter = databasesAdapter
            rvReceipts.adapter = receiptAdapter
            mainLabel.text =
                requireContext().getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE)
                    .getString(Constants.LAST_DATABASE_KEY, Constants.DEFAULT_DATABASE)
        }
        setObservers()
        setListeners()
    }

    private fun setObservers() {
        dbsViewModel.stateLiveData.observe(viewLifecycleOwner) { list ->
            databasesAdapter.submitList(list.toList())
        }
        setOrRefreshReceiptsObserver(receiptsViewModel, receiptAdapter)
    }

    private fun setOrRefreshReceiptsObserver(
        receiptsViewModel: ReceiptsViewModel,
        receiptAdapter: SimpleBindingAdapter<ReceiptListItem>
    ) {
        receiptsViewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            receiptAdapter.submitList(state.receipts)
            binding.selectOrClearAllTextView.setText(state.selectAllOperation.titleRes)
            if (state.totalCheckedCount > 0) {
                binding.btnDeleteSelected.visibility = View.VISIBLE
            } else {
                binding.btnDeleteSelected.visibility = View.GONE
            }
        }
    }


    private fun setListeners() {
        with(binding) {
            selectOrClearAllTextView.setOnClickListener {
                receiptsViewModel.selectOrClearAll()
            }
            btnDeleteSelected.setOnClickListener {
                receiptsViewModel.deleteSelectedReceipts()
            }
            btnToDbList.setOnClickListener {
                drawer.openDrawer(GravityCompat.START)
            }
            addReceipt.setOnClickListener { addReceipt() }
            btnAddDb.setOnClickListener {
                findNavController().navigate(ReceiptsListFragmentDirections.actionReceiptsListFragmentToAddDatabaseDialogFragment())
            }
            btnDeleteDbs.setOnClickListener {
                dbsViewModel.deleteAll()
            }
        }
    }

    private fun addReceipt() {
        findNavController().navigate(ReceiptsListFragmentDirections.actionReceiptsListFragmentToAddReceiptFragment())
    }

    private val databasesAdapter = databasesAdapter(object : DatabasesAdapterListener {
        override fun delete(name: String) {
            dbsViewModel.delete(name)
        }

        override fun click(name: String) {
            binding.mainLabel.text = name
            dbsViewModel.selectOrAdd(name)
            refreshReceiptsViewModel()
        }
    })

    private fun refreshReceiptsViewModel() {
        receiptsViewModel.stateLiveData.removeObservers(viewLifecycleOwner)
        viewModelStore.clear()

        val adapter = reloadAdapter()
        val receiptsVM = viewModels<ReceiptsViewModel>().value
        binding.rvReceipts.adapter = adapter
        setOrRefreshReceiptsObserver(receiptsVM, adapter)
        receiptsViewModel = receiptsVM
        receiptAdapter = adapter
    }

    private var receiptAdapter = reloadAdapter()

    private fun reloadAdapter() = receiptsSimpleAdapter(object : ReceiptsAdapterListener {
        override fun onReceiptDelete(receipt: ReceiptListItem) {
            receiptsViewModel.deleteReceipt(receipt)
        }

        override fun onReceiptToggle(receipt: ReceiptListItem) {
            receiptsViewModel.toggleSelection(receipt)
        }

        override fun onReceiptEdit(receipt: ReceiptListItem) {
            findNavController().navigate(
                ReceiptsListFragmentDirections.actionReceiptsListFragmentToEditReceiptFragment(
                    receipt.originReceipt
                )
            )
        }
    })


}
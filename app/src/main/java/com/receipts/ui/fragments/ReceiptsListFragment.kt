package com.receipts.ui.fragments

import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.elveum.elementadapter.SimpleBindingAdapter
import com.receipts.R
import com.receipts.databinding.DialogChangeDateBinding
import com.receipts.databinding.FragmentReceiptsBinding
import com.receipts.models.Repositories
import com.receipts.ui.lists.database.DatabasesViewModel
import com.receipts.ui.lists.database.adapter.DatabasesAdapterListener
import com.receipts.ui.lists.database.adapter.databasesAdapter
import com.receipts.ui.lists.receipts.ReceiptListItem
import com.receipts.ui.lists.receipts.ReceiptsViewModel
import com.receipts.ui.lists.receipts.adapter.ReceiptsAdapterListener
import com.receipts.ui.lists.receipts.adapter.receiptsSimpleAdapter
import com.receipts.utils.Constants
import com.receipts.utils.validators.DateValueValidator
import dagger.hilt.android.AndroidEntryPoint

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
        binding.checkDatabaseExist()
        receiptsViewModel = viewModels<ReceiptsViewModel>().value
        setAdapters()
        setObservers()
        setListeners()
    }

    private fun FragmentReceiptsBinding.checkDatabaseExist() {
        if (Repositories.dbsRepository.databasesList.isNotEmpty()) {
            mainLabel.text =
                requireContext().getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE)
                    .getString(Constants.LAST_DATABASE_KEY, Constants.DEFAULT_DATABASE)
        } else {
            dbsViewModel.selectOrAdd(Constants.DEFAULT_DATABASE)
            mainLabel.text = Constants.DEFAULT_DATABASE
        }
    }

    private fun setAdapters() {
        with(binding) {
            rvDbs.adapter = databasesAdapter
            rvReceipts.adapter = receiptAdapter
        }
    }

    private val databasesAdapter = databasesAdapter(object : DatabasesAdapterListener {
        override fun delete(name: String) {
            dbsViewModel.delete(name)
        }

        override fun click(name: String) {
            dbsViewModel.selectOrAdd(name)
        }
    })

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

    private fun setObservers() {
        setDatabasesObserver()
        setOrRefreshReceiptsObserver(receiptsViewModel, receiptAdapter)
    }

    private fun setDatabasesObserver() {
        dbsViewModel.stateLiveData.observe(viewLifecycleOwner) { list ->
            databasesAdapter.submitList(list.toList())
            if (list.isNullOrEmpty()) {
                binding.mainLabel.text = ""
                clearReceiptRecyclerView()
            } else {
                binding.mainLabel.text = Repositories.dbsRepository.database.openHelper.databaseName
                refreshReceiptsViewModel()
            }
        }
    }

    private fun setOrRefreshReceiptsObserver(
        receiptsViewModel: ReceiptsViewModel,
        receiptAdapter: SimpleBindingAdapter<ReceiptListItem>
    ) {
        receiptsViewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            receiptAdapter.submitList(state.receipts)
            with(binding) {
                selectOrClearAllTextView.setText(state.selectAllOperation.titleRes)
                if (state.totalCheckedCount > 0) {
                    btnDeleteSelected.visibility = View.VISIBLE
                    btnChangeSelectedDates.visibility = View.VISIBLE
                } else {
                    btnDeleteSelected.visibility = View.GONE
                    btnChangeSelectedDates.visibility = View.GONE
                }
            }
        }
    }

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

    private fun clearReceiptRecyclerView() {
        receiptsViewModel.stateLiveData.removeObservers(viewLifecycleOwner)
        viewModelStore.clear()
        receiptAdapter.submitList(listOf())
    }

    private fun setListeners() {
        with(binding) {
            selectOrClearAllTextView.setOnClickListener {
                receiptsViewModel.selectOrClearAll()
            }
            btnDeleteSelected.setOnClickListener {
                receiptsViewModel.deleteSelectedReceipts()
            }
            btnChangeSelectedDates.setOnClickListener {
                createAlertDialogForDatesChange()
            }
            btnToDbList.setOnClickListener {
                drawer.openDrawer(GravityCompat.START)
            }
            addReceipt.setOnClickListener {
                findNavController().navigate(ReceiptsListFragmentDirections.actionReceiptsListFragmentToAddReceiptFragment())
            }
            btnAddDb.setOnClickListener {
                findNavController().navigate(ReceiptsListFragmentDirections.actionReceiptsListFragmentToAddDatabaseDialogFragment())
            }
            btnDeleteDbs.setOnClickListener {
                dbsViewModel.deleteAll()
            }
        }
    }

    private fun createAlertDialogForDatesChange() {
        val dialogBinding = DialogChangeDateBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).create()
        dialog.show()
        dialogBinding.etNewDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                checkDateValueIntoWatcher(s)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                checkDateValueIntoWatcher(s)
            }

            private fun checkDateValueIntoWatcher(s: CharSequence?) {
                if (!DateValueValidator.checkDateValue(s.toString())) {
                    dialogBinding.etNewDate.error = getString(R.string.date_error)
                }
            }
        })
        dialogBinding.btnOk.setOnClickListener {
            val newDate = dialogBinding.etNewDate.text.toString()
            if (DateValueValidator.checkDateValue(newDate)) {
                receiptsViewModel.updateDateSelectedReceipts(newDate)
                dialog.dismiss()
            } else {
                Toast.makeText(context, R.string.date_error, Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
    }
}
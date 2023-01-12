package com.receipts.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.receipts.databinding.FragmentReceiptsBinding
import com.receipts.ui.lists.receipts.ReceiptListItem
import com.receipts.ui.lists.receipts.ReceiptsViewModel
import com.receipts.ui.lists.receipts.adapter.ReceiptsAdapterListener
import com.receipts.ui.lists.receipts.adapter.receiptsSimpleAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceiptsListFragment : Fragment() {

    private val receiptsViewModel by viewModels<ReceiptsViewModel>()
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

        binding.rvReceipts.adapter = receiptAdapter
        setObserver()
        setListeners()
    }

    private fun setObserver() {
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
        }
    }

    fun addReceipt() {
        findNavController().navigate(ReceiptsListFragmentDirections.actionReceiptsListFragmentToAddReceiptFragment())
    }

    private val receiptAdapter = receiptsSimpleAdapter(object : ReceiptsAdapterListener {
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
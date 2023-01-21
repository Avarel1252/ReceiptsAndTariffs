package com.receipts.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.receipts.R
import com.receipts.databinding.DialogChangeDateBinding
import com.receipts.ui.lists.receipts.ReceiptsViewModel
import com.receipts.utils.validators.DateValueValidator

class ChangeSelectedDateDialog : DialogFragment() {

    private lateinit var binding: DialogChangeDateBinding
    private val receiptsViewModel by viewModels<ReceiptsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogChangeDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            btnCancel.setOnClickListener { dismiss() }
            btnOk.setOnClickListener { validateNameValue() }
        }
    }

    private fun DialogChangeDateBinding.validateNameValue() {
        val newDate = etNewDate.text.toString()
        if (DateValueValidator.checkDateValue(newDate)) {
            receiptsViewModel.updateDateSelectedReceipts(newDate)
            dismiss()
        } else {
            Toast.makeText(context, R.string.date_error, Toast.LENGTH_SHORT).show()
        }
    }
}
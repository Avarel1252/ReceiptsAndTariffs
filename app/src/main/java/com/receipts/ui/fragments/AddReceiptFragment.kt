package com.receipts.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.receipts.databinding.FragmentAddReceiptBinding
import com.receipts.ui.lists.receipts.ReceiptsViewModel
import com.receipts.utils.entities.Receipt
import com.receipts.utils.entities.Tariff
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReceiptFragment : Fragment() {
    private lateinit var binding: FragmentAddReceiptBinding
    private val receiptsViewModel by viewModels<ReceiptsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
            btnSave.setOnClickListener {
                saveResult()
            }
        }
    }

    private fun saveResult() {
        with(binding) {
            if (isPassEdgeCases()) {
                receiptsViewModel.addReceipt(
                    Receipt(
                        name = etName.text.toString(),
                        date = etDate.text.toString(),
                        tariff = Tariff(
                            etV1.text.toString().toDouble(),
                            etV2.text.toString().toDouble(),
                            etV3.text.toString().toDouble(),
                            etV4.text.toString().toDouble(),
                            etV5.text.toString().toDouble(),
                            etV6.text.toString().toDouble(),
                        )
                    )
                )
                findNavController().navigateUp()
            }
        }
    }

    private fun isPassEdgeCases(): Boolean {
        with(binding) {
            var pass = 0
            pass += checkValue(tinV1, etV1)
            pass += checkValue(tinV2, etV2)
            pass += checkValue(tinV3, etV3)
            pass += checkValue(tinV4, etV4)
            pass += checkValue(tinV5, etV5)
            pass += checkValue(tinV6, etV6)
            if (pass == 6) {
                return true
            }
            return false
        }
    }

    fun checkValue(tIn: TextInputLayout, et: TextInputEditText): Int {
        if (!et.text.toString().matches("[0-9]{1,7}\\.?[0-9]{0,5}".toRegex())) {
            tIn.error = "Error"
            return 0
        }
        return 1
    }

}
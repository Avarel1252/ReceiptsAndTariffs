package com.receipts.ui.fragments

import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.style.TtsSpan.DateBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.receipts.R
import com.receipts.databinding.FragmentAddReceiptBinding
import com.receipts.databinding.FragmentEditReceiptBinding
import com.receipts.ui.lists.receipts.ReceiptsViewModel
import com.receipts.utils.CallbackTextWatcher
import com.receipts.utils.Constants
import com.receipts.utils.calculator.TariffCalculator
import com.receipts.utils.entities.Receipt
import com.receipts.utils.entities.Tariff
import com.receipts.utils.validators.DateValueValidator
import com.receipts.utils.validators.NameValueValidator
import com.receipts.utils.validators.TariffValueValidator
import dagger.hilt.android.AndroidEntryPoint
import java.time.Month
import java.util.Date
import java.util.jar.Attributes.Name

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
        setStartData()
        setListeners()
        setTextWatchers()
    }

    private fun setStartData() {
        with(binding) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val calendar = Calendar.getInstance()
                val month = (calendar.get(Calendar.MONTH) + 1).toString()
                val monthStr = if (month.length < 2) {
                    "0$month"
                } else {
                    month
                }
                val defDate = "${calendar.get(Calendar.YEAR)}." +
                        "${monthStr}." +
                        "${calendar.get(Calendar.DATE)}"
                etDate.setText(defDate)
            } else {
                etDate.setText(Constants.DEFAULT_DATE)
            }
            etV1.setText(Constants.DEFAULT_TARIFF_VALUE.toString())
            etV2.setText(Constants.DEFAULT_TARIFF_VALUE.toString())
            etV3.setText(Constants.DEFAULT_TARIFF_VALUE.toString())
            etV4.setText(Constants.DEFAULT_TARIFF_VALUE.toString())
            etV5.setText(Constants.DEFAULT_TARIFF_VALUE.toString())
            etV6.setText(Constants.DEFAULT_TARIFF_VALUE.toString())
            sumTariff.text = "${Constants.DEFAULT_TARIFF_VALUE}"
            checkNameError(etName.text)
            checkDateError(etDate.text)
        }
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
            if (isPassCases()) {
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
                            etV6.text.toString().toDouble()
                        )
                    )
                )
                findNavController().navigateUp()
            }
        }
    }

    private fun FragmentAddReceiptBinding.isPassCases() = isPassTariff() &&
            DateValueValidator.checkDateValue(etDate.text.toString()) &&
            NameValueValidator.checkNameValue(etName.text.toString())

    private fun FragmentAddReceiptBinding.isPassTariff() =
        TariffValueValidator.checkTariffValue(etV1.text.toString()) &&
                TariffValueValidator.checkTariffValue(etV2.text.toString()) &&
                TariffValueValidator.checkTariffValue(etV3.text.toString()) &&
                TariffValueValidator.checkTariffValue(etV4.text.toString()) &&
                TariffValueValidator.checkTariffValue(etV5.text.toString()) &&
                TariffValueValidator.checkTariffValue(etV6.text.toString())

    private fun setTextWatchers() {
        with(binding) {
            etV1.addTextChangedListener(watcherV1)
            etV2.addTextChangedListener(watcherV2)
            etV3.addTextChangedListener(watcherV3)
            etV4.addTextChangedListener(watcherV4)
            etV5.addTextChangedListener(watcherV5)
            etV6.addTextChangedListener(watcherV6)
            etName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                    checkNameError(s)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    checkNameError(s)
                }
            })
            etDate.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                    checkDateError(s)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    checkDateError(s)
                }
            })
        }
    }

    private val watcherV1 by lazy {
        CallbackTextWatcher(requireContext(), binding.etV1) { setTariffSumText() }
    }
    private val watcherV2 by lazy {
        CallbackTextWatcher(requireContext(), binding.etV2) { setTariffSumText() }
    }
    private val watcherV3 by lazy {
        CallbackTextWatcher(requireContext(), binding.etV3) { setTariffSumText() }
    }
    private val watcherV4 by lazy {
        CallbackTextWatcher(requireContext(), binding.etV4) { setTariffSumText() }
    }
    private val watcherV5 by lazy {
        CallbackTextWatcher(requireContext(), binding.etV5) { setTariffSumText() }
    }
    private val watcherV6 by lazy {
        CallbackTextWatcher(requireContext(), binding.etV6) { setTariffSumText() }
    }

    private fun setTariffSumText() {
        binding.sumTariff.text = getString(
            R.string.tariff_placeholder,
            TariffCalculator.tariffSum(
                watcherV1.result,
                watcherV2.result,
                watcherV3.result,
                watcherV4.result,
                watcherV5.result,
                watcherV6.result,
            ).toString()
        )
    }

    private fun FragmentAddReceiptBinding.checkDateError(s: CharSequence?) {
        if (!DateValueValidator.checkDateValue(s.toString())) {
            etDate.error = getString(R.string.date_error)
        }
    }

    private fun FragmentAddReceiptBinding.checkNameError(s: CharSequence?) {
        if (!NameValueValidator.checkNameValue(s.toString())) {
            etName.error = getString(R.string.empty_name)
        }
    }
}
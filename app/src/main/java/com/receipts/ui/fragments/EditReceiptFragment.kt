package com.receipts.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.receipts.R
import com.receipts.databinding.FragmentEditReceiptBinding
import com.receipts.ui.lists.receipts.ReceiptsViewModel
import com.receipts.utils.CallbackTextWatcher
import com.receipts.utils.calculator.TariffCalculator
import com.receipts.utils.entities.Tariff
import com.receipts.utils.validators.DateValueValidator
import com.receipts.utils.validators.NameValueValidator
import com.receipts.utils.validators.TariffValueValidator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditReceiptFragment : Fragment() {
    private lateinit var binding: FragmentEditReceiptBinding
    private val receiptsViewModel by viewModels<ReceiptsViewModel>()
    private val args by navArgs<EditReceiptFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditReceiptBinding.inflate(inflater, container, false)
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
            args.receipt.let { r ->
                etName.setText(r.name)
                etDate.setText(r.date)
                r.tariff.let { t ->
                    etV1.setText(t.value1.toString())
                    etV2.setText(t.value2.toString())
                    etV3.setText(t.value3.toString())
                    etV4.setText(t.value4.toString())
                    etV5.setText(t.value5.toString())
                    etV6.setText(t.value6.toString())
                    sumTariff.text = TariffCalculator.tariffSum(t).toString()
                    checkNameError(etName.text)
                    checkDateError(etDate.text)
                }
            }
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
                receiptsViewModel.update(
                    args.receipt.apply {
                        name = etName.text.toString()
                        date = etDate.text.toString()
                        tariff = Tariff(
                            etV1.text.toString().toDouble(),
                            etV2.text.toString().toDouble(),
                            etV3.text.toString().toDouble(),
                            etV4.text.toString().toDouble(),
                            etV5.text.toString().toDouble(),
                            etV6.text.toString().toDouble()
                        )
                    }
                )
                findNavController().navigateUp()
            }
        }
    }

    private fun FragmentEditReceiptBinding.isPassCases() = isPassTariff() &&
            DateValueValidator.checkDateValue(etDate.text.toString()) &&
            NameValueValidator.checkNameValue(etName.text.toString())

    private fun FragmentEditReceiptBinding.isPassTariff() =
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

    private fun FragmentEditReceiptBinding.checkDateError(s: CharSequence?) {
        if (!DateValueValidator.checkDateValue(s.toString())) {
            etDate.error = getString(R.string.date_error)
        }
    }

    private fun FragmentEditReceiptBinding.checkNameError(s: CharSequence?) {
        if (!NameValueValidator.checkNameValue(s.toString())) {
            etName.error = getString(R.string.empty_name)
        }
    }
}
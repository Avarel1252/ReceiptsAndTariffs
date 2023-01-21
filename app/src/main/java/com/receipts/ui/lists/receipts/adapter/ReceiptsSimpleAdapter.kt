package com.receipts.ui.lists.receipts.adapter

import android.view.View
import androidx.core.view.isGone
import com.elveum.elementadapter.getString
import com.elveum.elementadapter.simpleAdapter
import com.receipts.R
import com.receipts.databinding.ItemReceiptBinding
import com.receipts.ui.lists.receipts.ReceiptListItem
import com.receipts.utils.calculator.TariffCalculator
import com.receipts.utils.converter.TariffValuesConverter

fun receiptsSimpleAdapter(listener: ReceiptsAdapterListener) =
    simpleAdapter<ReceiptListItem, ItemReceiptBinding> {
        areItemsSame = { oldCat, newCat -> oldCat.id == newCat.id }

        bind { receipt ->
            tvName.text = getString(R.string.name_placeholder, receipt.name)
            tvDate.text = getString(R.string.date_placeholder, receipt.date)
            tvTariff.text = getString(
                R.string.tariff_placeholder,
                TariffCalculator.tariffSum(receipt.tariff).toString()
            )
            tvV1.text = getString(
                R.string.v1_placeholder,
                TariffValuesConverter.normalizeStringValue(receipt.tariff.value1)
            )
            tvV2.text = getString(
                R.string.v2_placeholder,
                TariffValuesConverter.normalizeStringValue(receipt.tariff.value2)
            )
            tvV3.text = getString(
                R.string.v3_placeholder,
                TariffValuesConverter.normalizeStringValue(receipt.tariff.value3)
            )
            tvV4.text = getString(
                R.string.v4_placeholder,
                TariffValuesConverter.normalizeStringValue(receipt.tariff.value4)
            )
            tvV5.text = getString(
                R.string.v5_placeholder,
                TariffValuesConverter.normalizeStringValue(receipt.tariff.value5)
            )
            tvV6.text = getString(
                R.string.v6_placeholder,
                TariffValuesConverter.normalizeStringValue(receipt.tariff.value6)
            )
            checkbox.isChecked = receipt.isChecked
        }

        listeners {
            deleteImageView.onClick { listener.onReceiptDelete(it) }
            root.onClick {
                listener.onReceiptEdit(it)
            }
            checkbox.onClick {
                listener.onReceiptToggle(it)
            }
            btnTariff.onClick {
                if (flowTariff.isGone) {
                    btnTariff.setBackgroundResource(R.drawable.arrow_close)
                    flowTariff.visibility = View.VISIBLE
                } else {
                    btnTariff.setBackgroundResource(R.drawable.arrow_open)
                    flowTariff.visibility = View.GONE
                }
            }
        }
    }
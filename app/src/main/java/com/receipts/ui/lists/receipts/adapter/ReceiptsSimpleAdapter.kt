package com.receipts.ui.lists.receipts.adapter

import android.view.View
import androidx.core.view.isGone
import com.elveum.elementadapter.simpleAdapter
import com.receipts.R
import com.receipts.databinding.ItemReceiptBinding
import com.receipts.ui.lists.receipts.ReceiptListItem
import com.receipts.utils.calculator.TariffCalculator

fun receiptsSimpleAdapter(listener: ReceiptsAdapterListener) =
    simpleAdapter<ReceiptListItem, ItemReceiptBinding> {
        areItemsSame = { oldCat, newCat -> oldCat.id == newCat.id }

        bind { receipt ->
            tvName.text = receipt.name
            tvDate.text = receipt.date
            tvTariff.text = TariffCalculator.tariffSum(receipt.tariff).toString()
            tvV1.text = receipt.tariff.value1.toString()
            tvV2.text = receipt.tariff.value2.toString()
            tvV3.text = receipt.tariff.value3.toString()
            tvV4.text = receipt.tariff.value4.toString()
            tvV5.text = receipt.tariff.value5.toString()
            tvV6.text = receipt.tariff.value6.toString()
            checkbox.isChecked = receipt.isChecked
        }

        listeners {
            deleteImageView.onClick { listener.onReceiptDelete(it) }
            root.onLongClick {
                listener.onReceiptEdit(it)
                true
            }
            checkbox.onClick {
                listener.onReceiptToggle(it)
            }
            btnTariff.onClick {
                if (flowTariff.isGone) {
                    btnTariff.setBackgroundResource(R.drawable.arrow_close)
                    flowTariff.visibility = View.VISIBLE
                    btnEditTariff.visibility = View.VISIBLE
                } else {
                    btnTariff.setBackgroundResource(R.drawable.arrow_open)
                    flowTariff.visibility = View.GONE
                    btnEditTariff.visibility = View.GONE
                }
            }
        }
    }
package com.receipts.ui.lists.receipts

import com.receipts.utils.entities.Receipt
import com.receipts.utils.entities.Tariff

data class ReceiptListItem(
    val originReceipt: Receipt,
    val isChecked: Boolean
) {
    val id: Long = originReceipt.id!!
    val name: String = originReceipt.name
    val date: String = originReceipt.date
    val tariff: Tariff = originReceipt.tariff
}
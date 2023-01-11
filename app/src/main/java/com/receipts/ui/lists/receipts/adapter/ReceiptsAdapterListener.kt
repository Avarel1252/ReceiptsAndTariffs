package com.receipts.ui.lists.receipts.adapter

import com.receipts.ui.lists.receipts.ReceiptListItem

interface ReceiptsAdapterListener {
    fun onReceiptDelete(receipt: ReceiptListItem)
    fun onReceiptToggle(receipt: ReceiptListItem)
    fun onReceiptEdit(receipt: ReceiptListItem)
}
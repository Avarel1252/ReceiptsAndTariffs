package com.receipts.utils

import androidx.recyclerview.widget.DiffUtil
import com.receipts.utils.entities.Receipt

object ItemCallbackDiffUtil : DiffUtil.ItemCallback<Receipt>() {
    override fun areItemsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
        return oldItem == newItem
    }
}

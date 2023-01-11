package com.receipts.utils.multichoice


interface MultiChoiceState<T> {
    fun isChecked(item: T): Boolean
    val totalCheckedCount: Int
}
package com.receipts.utils.converter

interface ITariffValuesConverter {
    fun normalizeStringValue(value: Double): String
}
package com.receipts.utils.converter

object TariffValuesConverter : ITariffValuesConverter {
    override fun normalizeStringValue(value: Double): String {
        val strValue = value.toString()
        val intPart = strValue.substringBefore(".")
        val decimalPart = strValue.substringAfter(".")
        val builder = StringBuilder()
        for (i in 1..(7 - intPart.length)) {
            builder.append("0")
        }
        builder.append("$intPart.$decimalPart")
        return builder.toString()
    }

}
package com.receipts.utils.validators

object TariffValueValidator : ITariffValidator {
    override fun checkTariffValue(strTariffValue: String): Boolean =
        (strTariffValue.matches("[0-9]{1,7}\\.[0-9]{1,5}".toRegex()))
                || (strTariffValue.matches("[0-9]{1,7}".toRegex()))

}
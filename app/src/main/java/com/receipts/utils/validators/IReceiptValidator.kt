package com.receipts.utils.validators

interface IDateValidator {
    fun checkDateValue(date: String): Boolean
}

interface INameValidator {
    fun checkNameValue(name: String): Boolean
}

interface ITariffValidator {
    fun checkTariffValue(strTariffValue: String): Boolean
}
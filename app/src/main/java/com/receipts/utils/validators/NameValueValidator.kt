package com.receipts.utils.validators

object NameValueValidator : INameValidator {
    override fun checkNameValue(name: String) = name.isNotBlank()
}
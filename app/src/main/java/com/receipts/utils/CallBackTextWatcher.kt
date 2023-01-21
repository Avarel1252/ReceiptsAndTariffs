package com.receipts.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.receipts.R
import com.receipts.utils.validators.TariffValueValidator

class CallbackTextWatcher(
    private val context: Context,
    private val editText: TextInputEditText,
    private val sumFunction: () -> Unit
) : TextWatcher {
    var result = 0.0
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (!TariffValueValidator.checkTariffValue(s.toString())) {
            editText.error = context.getString(R.string.error)
        } else {
            editText.error = ""
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        result = 0.0
    }

    override fun afterTextChanged(s: Editable?) {
        val str = s.toString()
        if (TariffValueValidator.checkTariffValue(str)) {
            result = str.toDouble()
            sumFunction.invoke()
        } else {
            editText.error = context.getString(R.string.error)
        }
    }
}

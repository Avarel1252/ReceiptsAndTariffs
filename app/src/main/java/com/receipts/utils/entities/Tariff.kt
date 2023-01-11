package com.receipts.utils.entities

import android.os.Parcelable
import com.receipts.utils.Constants
import com.receipts.utils.calculator.TariffCalculator
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tariff(
    var value1: Double = Constants.DEFAULT_TARIFF_VALUE,
    var value2: Double = Constants.DEFAULT_TARIFF_VALUE,
    var value3: Double = Constants.DEFAULT_TARIFF_VALUE,
    var value4: Double = Constants.DEFAULT_TARIFF_VALUE,
    var value5: Double = Constants.DEFAULT_TARIFF_VALUE,
    var value6: Double = Constants.DEFAULT_TARIFF_VALUE,
) : Parcelable
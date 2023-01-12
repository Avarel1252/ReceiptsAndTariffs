package com.receipts.utils.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.receipts.utils.Constants
import com.receipts.utils.calculator.TariffCalculator
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tariff(
    @ColumnInfo var value1: Double = Constants.DEFAULT_TARIFF_VALUE,
    @ColumnInfo var value2: Double = Constants.DEFAULT_TARIFF_VALUE,
    @ColumnInfo var value3: Double = Constants.DEFAULT_TARIFF_VALUE,
    @ColumnInfo var value4: Double = Constants.DEFAULT_TARIFF_VALUE,
    @ColumnInfo var value5: Double = Constants.DEFAULT_TARIFF_VALUE,
    @ColumnInfo var value6: Double = Constants.DEFAULT_TARIFF_VALUE,
) : Parcelable
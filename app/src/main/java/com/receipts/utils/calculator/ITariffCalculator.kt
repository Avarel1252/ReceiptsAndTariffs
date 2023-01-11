package com.receipts.utils.calculator

import com.receipts.utils.entities.Tariff

interface ITariffCalculator {
    fun tariffSum(
        value1: Double,
        value2: Double,
        value3: Double,
        value4: Double,
        value5: Double,
        value6: Double,
    ) :Double

    fun tariffSum(
        tariff: Tariff
    ) :Double
}
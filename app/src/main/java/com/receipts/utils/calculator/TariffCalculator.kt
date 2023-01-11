package com.receipts.utils.calculator

import com.receipts.utils.entities.Tariff

object TariffCalculator : ITariffCalculator {

    override fun tariffSum(tariff: Tariff) =
        with(tariff) { value1 + value2 + value3 + value4 + value5 + value6 }


    override fun tariffSum(
        value1: Double,
        value2: Double,
        value3: Double,
        value4: Double,
        value5: Double,
        value6: Double
    ) = (value1 + value2 + value3 + value4 + value5 + value6)
}
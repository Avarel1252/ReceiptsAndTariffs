package com.receipts.utils.validators

object DateValueValidator : IDateValidator {
    override fun checkDateValue(date: String): Boolean {
        if (date.matches("[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}".toRegex())) {
            val year = date.substringBefore('.').toInt()
            println("year $year")
            val month = date.substringAfter('.').substringBefore('.').toInt()
            println("month $month")
            val day = date.substringAfterLast('.').toInt()
            println("day $day")
            if (year in 1900..3000 &&
                month in 1..12 &&
                day in 1..31
            ) {
                return true
            }
        }
        return false
    }
}
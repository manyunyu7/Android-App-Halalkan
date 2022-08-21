package com.feylabs.halalkan.utils.resto

import java.text.DecimalFormat
import java.text.NumberFormat

data class OrderSummaryModel(
    val quantity: Int = 0,
    var totalInWon: Double = 0.0,
    var totalMenu: Int = 0
) {
    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

    fun getFormattedTotalPrice(): String {
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = this.totalInWon
        val formattedNumber: String = formatter.format(myNumber)
        return formattedNumber + " won"
    }
}


package com.feylabs.halalkan.utils.resto

import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import java.text.DecimalFormat
import java.text.NumberFormat

data class OrderLocalModel(
    val menuId: String,
    var notes:String,
    val quantity:Int=0,
    var price:Double,
    var restoId:Int,
    var food:FoodModelResponse
){
    fun getOrderedItems(){
        var listString = ""
    }

    //food x quantity
    fun getFormattedFoodTotal(): String {
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = this.food.price * this.quantity
        val formattedNumber: String = formatter.format(myNumber)
        return formattedNumber + " won"
    }
}
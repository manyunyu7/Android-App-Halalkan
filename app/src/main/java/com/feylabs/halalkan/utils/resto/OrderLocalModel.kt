package com.feylabs.halalkan.utils.resto

data class OrderLocalModel(
    val menuId: String,
    val notes:String,
    val quantity:Int=0,
    var price:Double
)
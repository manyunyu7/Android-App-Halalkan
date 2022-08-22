package com.feylabs.halalkan.data.remote.reqres.order


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CreateCartPayload(
    var restoId: String = "",
    @SerializedName("address")
    var address: String = "",
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("long")
    var long: Double,
    @SerializedName("orders")
    var orders: List<Order> = listOf()
) {
    @Keep
    data class Order(
        @SerializedName("food_id")
        var foodId: Int = 0,
        @SerializedName("notes")
        var notes: String = "",
        @SerializedName("quantity")
        var quantity: Int = 0
    )
}
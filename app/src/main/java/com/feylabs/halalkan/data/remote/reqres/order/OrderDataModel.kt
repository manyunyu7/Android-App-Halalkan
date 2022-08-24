package com.feylabs.halalkan.data.remote.reqres.order

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderDataModel(
    @SerializedName("customer")
    var customer: String = "",
    @SerializedName("customer_id")
    var customerId: Int = 0,
    @SerializedName("food")
    var food: String = "",
    @SerializedName("food_id")
    var foodId: Int = 0,
    @SerializedName("food_image")
    var foodImage: String = "",
    @SerializedName("food_price")
    var foodPrice: Int = 0,
    @SerializedName("notes")
    var notes: String? = "",
    @SerializedName("quantity")
    var quantity: Int = 0
){}
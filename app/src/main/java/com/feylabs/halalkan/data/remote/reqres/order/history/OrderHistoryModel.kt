package com.feylabs.halalkan.data.remote.reqres.order.history

import androidx.annotation.Keep
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import com.google.gson.annotations.SerializedName
import java.text.DecimalFormat
import java.text.NumberFormat

@Keep
data class OrderHistoryModel(
    @SerializedName("address")
    var address: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("driver_id")
    var driverId: Any? = Any(),
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("lat")
    var lat: String = "",
    @SerializedName("long")
    var long: String = "",
    @SerializedName("orders")
    var orders: List<Order> = listOf(),
    @SerializedName("resto_obj")
    var restoObj: RestoModelResponse = RestoModelResponse(),
    @SerializedName("user_obj")
    var userObj: UserModel? = null,
    @SerializedName("resto_id")
    var restoId: Int = 0,
    @SerializedName("status_desc")
    var statusDesc: String = "",
    @SerializedName("status_id")
    var statusId: Int = 0,
    @SerializedName("total_price")
    var totalPrice: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("user_id")
    var userId: Int = 0,
    @SerializedName("user_sign")
    var userSign: Any? = Any()
) {

    fun getFormattedTotalPrice(): String {
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = this.totalPrice
        val formattedNumber: String = formatter.format(myNumber)
        return "$formattedNumber won"
    }

    fun getOrdersString(): String {
        var count = 0
        var text = ""
        orders.forEachIndexed { index, orderLocalModel ->
            count += 1
            val name = orderLocalModel.food
            val quantity = orderLocalModel.quantity
            text += "$count. $name x${quantity}   ${orderLocalModel.getFormattedFoodTotal()}\n"
        }

        return text
    }

    @Keep
    data class Order(
        @SerializedName("customer")
        var customer: String = "",
        @SerializedName("customer_id")
        var customerId: Int = 0,
        @SerializedName("food")
        var food: String = "",
        @SerializedName("food_price")
        var food_price: Double = 0.0,
        @SerializedName("food_id")
        var foodId: Int = 0,
        @SerializedName("food_image")
        var foodImage: String = "",
        @SerializedName("notes")
        var notes: String? = "",
        @SerializedName("quantity")
        var quantity: Int = 0
    ){
        //food x quantity
        fun getFormattedFoodTotal(): String {
            val formatter: NumberFormat = DecimalFormat("#,###")
            val myNumber = this.food_price * this.quantity
            val formattedNumber: String = formatter.format(myNumber)
            return "$formattedNumber won"
        }
    }
}
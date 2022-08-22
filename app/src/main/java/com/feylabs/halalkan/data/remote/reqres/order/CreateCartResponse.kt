package com.feylabs.halalkan.data.remote.reqres.order


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CreateCartResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {
    @Keep
    data class Data(
        @SerializedName("order")
        var order: Order = Order(),
        @SerializedName("totalPrice")
        var totalPrice: Int = 0
    ) {
        @Keep
        data class Order(
            @SerializedName("address")
            var address: String = "",
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("lat")
            var lat: String = "",
            @SerializedName("long")
            var long: String = "",
            @SerializedName("orders")
            var orders: List<Order> = listOf(),
            @SerializedName("resto_id")
            var restoId: String = "",
            @SerializedName("status_desc")
            var statusDesc: String = "",
            @SerializedName("status_id")
            var statusId: Int = 0,
            @SerializedName("total_price")
            var totalPrice: Int = 0,
            @SerializedName("updated_at")
            var updatedAt: String = "",
            @SerializedName("user_id")
            var userId: Int = 0
        ) {
            @Keep
            data class Order(
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
                @SerializedName("notes")
                var notes: String = "",
                @SerializedName("quantity")
                var quantity: Int = 0
            )
        }
    }
}
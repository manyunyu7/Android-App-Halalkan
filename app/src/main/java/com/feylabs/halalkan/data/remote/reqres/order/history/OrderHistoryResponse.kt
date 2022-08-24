package com.feylabs.halalkan.data.remote.reqres.order.history


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class OrderHistoryResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var orderData: List<OrderHistoryModel> = listOf(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {

}
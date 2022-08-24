package com.feylabs.halalkan.data.remote.reqres.order


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

class OrderStatusResponse : ArrayList<OrderStatusResponse.OrderStatusResponseItem>(){
    @Keep
    data class OrderStatusResponseItem(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = ""
    )
}
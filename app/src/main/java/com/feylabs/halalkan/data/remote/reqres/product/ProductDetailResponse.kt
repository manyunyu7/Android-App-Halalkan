package com.feylabs.halalkan.data.remote.reqres.product


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ProductDetailResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: ProductModelResponse = ProductModelResponse(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {
}
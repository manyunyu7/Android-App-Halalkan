package com.feylabs.halalkan.data.remote.reqres.resto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AllRestoNoPagination(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: List<RestoModelResponse> = listOf(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {

}
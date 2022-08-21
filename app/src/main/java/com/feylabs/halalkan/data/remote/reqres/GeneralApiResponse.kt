package com.feylabs.halalkan.data.remote.reqres


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class GeneralApiResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
)
package com.feylabs.halalkan.data.remote.reqres.driver


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class MyProfileDriverResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: DriverObj = DriverObj(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {

}
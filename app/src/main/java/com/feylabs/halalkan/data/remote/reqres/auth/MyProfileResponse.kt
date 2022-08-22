package com.feylabs.halalkan.data.remote.reqres.auth


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class MyProfileResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var userModel: UserModel,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {
}
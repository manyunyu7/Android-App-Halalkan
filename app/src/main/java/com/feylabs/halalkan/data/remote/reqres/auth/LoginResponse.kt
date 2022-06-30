package com.feylabs.halalkan.data.remote.reqres.auth


import androidx.annotation.Nullable
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    val mCode: Int,
    @SerializedName("user")
    val user: UserModel?,
    @SerializedName("message")
    val mMessage: String? = "",
    @SerializedName("access_token")
    val accessToken: String? = "",
    @SerializedName("success")
    val success: Boolean
) {
}
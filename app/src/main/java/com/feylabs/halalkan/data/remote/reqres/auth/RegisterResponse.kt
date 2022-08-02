package com.feylabs.halalkan.data.remote.reqres.auth


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("data")
    val user: UserModel
) {
}
package com.feylabs.halalkan.data.remote.reqres.auth


import com.google.gson.annotations.SerializedName

data class RegisterBodyRequest(
    @SerializedName("confirm_password")
    val confirmPassword: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("roles_id")
    val rolesId: Int
)
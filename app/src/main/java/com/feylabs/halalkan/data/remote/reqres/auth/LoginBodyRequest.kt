package com.feylabs.halalkan.data.remote.reqres.auth


import com.google.gson.annotations.SerializedName

data class LoginBodyRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
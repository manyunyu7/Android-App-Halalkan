package com.feylabs.halalkan.data.remote.reqres


import com.google.gson.annotations.SerializedName

data class UserBasicResponse(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("photo")
    val photo: String? = "",
    @SerializedName("roles_id")
    val rolesId: Int,
    @SerializedName("updated_at")
    val updatedAt: String
)
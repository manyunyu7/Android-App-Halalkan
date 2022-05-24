package com.feylabs.halalkan.data.remote.reqres.auth


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("user")
    val user: User
) {
    data class User(
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
        val photo: Any,
        @SerializedName("roles_id")
        val rolesId: Int,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}
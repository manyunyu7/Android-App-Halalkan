package com.feylabs.halalkan.data.remote.reqres


import com.google.gson.annotations.SerializedName

data class PostDetailResponse(
    @SerializedName("body")
    val body: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: Int
)
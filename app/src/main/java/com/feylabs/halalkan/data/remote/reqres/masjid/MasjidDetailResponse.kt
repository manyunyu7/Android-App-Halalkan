package com.feylabs.halalkan.data.remote.reqres.masjid


import com.google.gson.annotations.SerializedName

data class MasjidDetailResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<MasjidModelResponse>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
) {
}
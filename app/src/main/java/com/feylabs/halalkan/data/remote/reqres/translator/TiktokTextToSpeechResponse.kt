package com.feylabs.halalkan.data.remote.reqres.translator


import com.google.gson.annotations.SerializedName

data class TiktokTextToSpeechResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("error")
    val error: String,
    @SerializedName("success")
    val success: Boolean,
) {
}
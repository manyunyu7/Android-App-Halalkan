package com.feylabs.halalkan.data.remote.reqres.translator


import com.google.gson.annotations.SerializedName

data class TiktokTextToSpeechResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("extra")
    val extra: Extra,
    @SerializedName("message")
    val message: String,
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_msg")
    val statusMsg: String
) {
    data class Data(
        @SerializedName("duration")
        val duration: String,
        @SerializedName("s_key")
        val sKey: String,
        @SerializedName("v_str")
        val vStr: String
    )

    data class Extra(
        @SerializedName("log_id")
        val logId: String
    )
}
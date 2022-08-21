package com.feylabs.halalkan.data.remote.reqres.forum


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ForumDetailResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var forum: ForumModelResponse,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {
}
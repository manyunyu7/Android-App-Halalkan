package com.feylabs.halalkan.data.remote.reqres.forum


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AddCommentResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {
    @Keep
    data class Data(
        @SerializedName("comment")
        var comment: String = "",
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("forum_id")
        var forumId: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("updated_at")
        var updatedAt: String = "",
        @SerializedName("user_id")
        var userId: Int = 0
    )
}
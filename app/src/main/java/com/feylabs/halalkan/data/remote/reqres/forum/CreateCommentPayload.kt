package com.feylabs.halalkan.data.remote.reqres.forum


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CreateCommentPayload(
    @SerializedName("comment")
    var comment: String = "",
    @SerializedName("forum_id")
    var forumId: Int
)
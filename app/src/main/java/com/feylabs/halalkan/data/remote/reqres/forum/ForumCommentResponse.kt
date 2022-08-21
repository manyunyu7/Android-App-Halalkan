package com.feylabs.halalkan.data.remote.reqres.forum


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

class ForumCommentResponse : ArrayList<ForumCommentResponse.ForumCommentResponseItem>(){
    @Keep
    data class ForumCommentResponseItem(
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
        @SerializedName("user")
        var user: User = User(),
        @SerializedName("user_id")
        var userId: Int = 0,
        @SerializedName("likes")
        var likes: List<Like>? = listOf(),
    ) {

        fun getLikeCount() : Int{
            likes?.let {
                return it.size
            } ?: kotlin.run {
                return 0
            }
        }

        @Keep
        data class Like(
            @SerializedName("comment_id")
            var commentId: Int = 0,
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("updated_at")
            var updatedAt: String = "",
            @SerializedName("user_id")
            var userId: Int = 0
        )

        @Keep
        data class User(
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("email")
            var email: String = "",
            @SerializedName("email_verified_at")
            var emailVerifiedAt: Any? = Any(),
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("img_full_path")
            var imgFullPath: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("phone_number")
            var phoneNumber: String = "",
            @SerializedName("photo")
            var photo: Any? = Any(),
            @SerializedName("roles_id")
            var rolesId: Int = 0,
            @SerializedName("updated_at")
            var updatedAt: String = ""
        )
    }
}
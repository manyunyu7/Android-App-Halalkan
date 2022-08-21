package com.feylabs.halalkan.data.remote.reqres.forum

import androidx.annotation.Keep
import androidx.annotation.Nullable
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.google.gson.annotations.SerializedName

@Keep
data class ForumModelResponse(
    @SerializedName("body")
    var body: String = "",
    @SerializedName("category")
    var category: Category? = Category(),
    @SerializedName("category_id")
    var categoryId: Int = 0,
    @SerializedName("comments")
    var comments: List<Comment>? = listOf(),
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("img")
    var img: String? = "",
    @SerializedName("img_full_path")
    var img_full_path: String = "",
    @SerializedName("likes")
    var likes: List<Like>? = listOf(),
    @SerializedName("title")
    var title: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("user")
    var user: UserModel?,
    @SerializedName("user_id")
    var userId: Int = 0,
    @Nullable
    val ViewType: Int = PaginationPlaceholder.VNormal,
    var isFooterVisible: Boolean = true,
) {

    fun getLikeCount() : Int{
        likes?.let {
          return it.size
        } ?: kotlin.run {
            return 0
        }
    }

    fun getCommentCount() : Int{
        comments?.let {
          return it.size
        } ?: kotlin.run {
            return 0
        }
    }

    @Keep
    data class Category(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = ""
    )

    @Keep
    data class Comment(
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

    @Keep
    data class Like(
        @SerializedName("forum_id")
        var forumId: Int = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("user_id")
        var userId: Int = 0
    )
}

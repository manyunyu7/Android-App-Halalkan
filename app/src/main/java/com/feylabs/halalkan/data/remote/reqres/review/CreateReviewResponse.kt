package com.feylabs.halalkan.data.remote.reqres.review


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CreateReviewResponse(
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
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("masjid_id")
        var masjidId: String = "",
        @SerializedName("rating_id")
        var ratingId: String = "",
        @SerializedName("review_category")
        var reviewCategory: String = "",
        @SerializedName("review_photos")
        var reviewPhotos: List<String> = listOf(),
        @SerializedName("updated_at")
        var updatedAt: String = "",
        @SerializedName("user_id")
        var userId: Int = 0,
        @SerializedName("user_info")
        var userInfo: UserInfo = UserInfo()
    ) {
        @Keep
        data class UserInfo(
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("email")
            var email: String = "",
            @SerializedName("email_verified_at")
            var emailVerifiedAt: Any? = Any(),
            @SerializedName("id")
            var id: Int = 0,
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
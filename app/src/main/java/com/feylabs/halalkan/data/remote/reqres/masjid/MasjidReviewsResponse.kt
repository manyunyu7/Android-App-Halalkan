package com.feylabs.halalkan.data.remote.reqres.masjid


import com.google.gson.annotations.SerializedName

data class MasjidReviewsResponse(
    @SerializedName("review_count")
    val reviewCount: ReviewCount,
    @SerializedName("reviews")
    val reviews: List<Review>
) {
    data class ReviewCount(
        @SerializedName("rating1")
        val rating1: Int,
        @SerializedName("rating2")
        val rating2: Int,
        @SerializedName("rating3")
        val rating3: Int,
        @SerializedName("rating4")
        val rating4: Int,
        @SerializedName("rating5")
        val rating5: Int
    )

    data class Review(
        @SerializedName("comment")
        val comment: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("masjid_id")
        val masjidId: Int,
        @SerializedName("rating_id")
        val ratingId: Int,
        @SerializedName("review_category")
        val reviewCategory: String,
        @SerializedName("review_photos")
        val reviewPhotos: List<String>,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int
    )
}
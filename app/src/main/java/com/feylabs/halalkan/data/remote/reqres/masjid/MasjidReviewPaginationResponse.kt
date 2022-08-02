package com.feylabs.halalkan.data.remote.reqres.masjid


import androidx.annotation.Nullable
import com.feylabs.halalkan.data.remote.reqres.UserBasicResponse
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.google.gson.annotations.SerializedName

data class MasjidReviewPaginationResponse(
    @SerializedName("review_count")
    val reviewCount: ReviewCount,
    @SerializedName("reviews")
    val reviews: Reviews,
) {
    data class ReviewCount(
        @SerializedName("avg")
        val avg: Double,
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

    data class Reviews(
        @SerializedName("current_page")
        var currentPage: Int,
        @SerializedName("data")
        val `data`: List<Data>,
        @SerializedName("first_page_url")
        val firstPageUrl: String,
        @SerializedName("from")
        val from: Int,
        @SerializedName("last_page")
        val lastPage: Int,
        @SerializedName("last_page_url")
        val lastPageUrl: String,
        @SerializedName("links")
        val links: List<Link>,
        @SerializedName("next_page_url")
        val nextPageUrl: String,
        @SerializedName("path")
        val path: String,
        @SerializedName("per_page")
        val perPage: String,
        @SerializedName("prev_page_url")
        val prevPageUrl: Any,
        @SerializedName("to")
        val to: Int,
        @SerializedName("total")
        val total: Int
    ) {
        data class Data(
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
            val reviewPhotos: List<Any>,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("user_id")
            val userId: Int,
            @SerializedName("user_info")
            val userInfo : UserBasicResponse? = null,
            //pagination element UI
            @Nullable
            val ViewType: Int = PaginationPlaceholder.VNormal,
            var isFooterVisible: Boolean = true,
        ){

        }

        data class Link(
            @SerializedName("active")
            val active: Boolean,
            @SerializedName("label")
            val label: String,
            @SerializedName("url")
            val url: Any
        )
    }
}
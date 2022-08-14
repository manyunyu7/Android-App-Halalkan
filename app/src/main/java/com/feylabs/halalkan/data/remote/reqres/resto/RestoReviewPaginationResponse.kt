package com.feylabs.halalkan.data.remote.reqres.resto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.annotation.Nullable
import com.feylabs.halalkan.data.remote.reqres.UserBasicResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse.ReviewCount
import com.feylabs.halalkan.utils.PaginationPlaceholder

@Keep
data class RestoReviewPaginationResponse(
    @SerializedName("review_count")
    var reviewCount: ReviewCount,
    @SerializedName("reviews")
    var reviews: Reviews = Reviews()
) {

    @Keep
    data class Reviews(
        @SerializedName("current_page")
        var currentPage: Int = 0,
        @SerializedName("data")
        var `data`: List<Data> = listOf(),
        @SerializedName("first_page_url")
        var firstPageUrl: String = "",
        @SerializedName("from")
        var from: Int = 0,
        @SerializedName("last_page")
        var lastPage: Int = 0,
        @SerializedName("last_page_url")
        var lastPageUrl: String = "",
        @SerializedName("links")
        var links: List<Link> = listOf(),
        @SerializedName("next_page_url")
        var nextPageUrl: Any? = Any(),
        @SerializedName("path")
        var path: String = "",
        @SerializedName("per_page")
        var perPage: Int = 0,
        @SerializedName("prev_page_url")
        var prevPageUrl: Any? = Any(),
        @SerializedName("to")
        var to: Int = 0,
        @SerializedName("total")
        var total: Int = 0
    ) {
        @Keep
        data class Data(
            @SerializedName("comment")
            var comment: String = "",
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("rating_id")
            var ratingId: Int = 0,
            @SerializedName("restoran_id")
            var restoranId: Int = 0,
            @SerializedName("review_category")
            val reviewCategory: String,
            @SerializedName("review_photos")
            val reviewPhotos: List<Any>,
            @SerializedName("updated_at")
            var updatedAt: String = "",
            @SerializedName("user_id")
            var userId: Int = 0,
            @SerializedName("user_info")
            val userInfo : UserBasicResponse? = null,
            //pagination element UI
            @Nullable
            val ViewType: Int = PaginationPlaceholder.VNormal,
            var isFooterVisible: Boolean = true,
        )

        @Keep
        data class Link(
            @SerializedName("active")
            var active: Boolean = false,
            @SerializedName("label")
            var label: String = "",
            @SerializedName("url")
            var url: String? = ""
        )
    }
}
package com.feylabs.halalkan.utils

import com.feylabs.halalkan.data.remote.reqres.forum.ForumModelResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidModelResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoReviewPaginationResponse


class PaginationPlaceholder {
    companion object {
        const val VNormal = 2
        const val VFooter = 3
        fun getMasjidReviewPaginationResponsePlaceHolder() =
            MasjidReviewPaginationResponse.Reviews.Data(
                id = 0,
                masjidId = 0,
                ratingId = 0,
                reviewCategory = "",
                comment = "",
                createdAt = "",
                updatedAt = "",
                reviewPhotos = mutableListOf(),
                userId = 0,
                userInfo = null,
                ViewType = VFooter
            )

        fun getMasjidDataPaginationResponsePlaceHolder() = MasjidModelResponse(
            id = 0,
            createdAt = "",
            updatedAt = "",
            address = "",
            categoryName = "",
            distanceKm = "",
            distanceKmDouble = 0.0,
            distanceKmDoubleRounded = 0.0,
            facilities = "",
            img = "",
            lat = "",
            long = "",
            name = "",
            operatingStart = "",
            operatingEnd = "",
            phone = "",
            review_avg = "",
            typeId = 0,
            review_count = "",
            img_full_path = "",
            ViewType = VFooter
        )

        fun getForumPlaceholder() = ForumModelResponse(
            id = 0,
            createdAt = "",
            updatedAt = "",
            user = null,
            category = null,
            title = "",
            userId = 0,
            body = "",
            img = "",
            likes = null,
            categoryId =0,
            comments = null,
            img_full_path = "",
            ViewType = VFooter
        )

        fun getRestoReviewPaginationResponsePlaceHolder(): RestoReviewPaginationResponse.Reviews.Data {
            return RestoReviewPaginationResponse.Reviews.Data(
                id = 0,
                restoranId = 0,
                ratingId = 0,
                reviewCategory = "",
                comment = "",
                createdAt = "",
                updatedAt = "",
                reviewPhotos = mutableListOf(),
                userId = 0,
                userInfo = null,
                ViewType = VFooter
            )
        }
    }
}
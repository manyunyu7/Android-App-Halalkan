package com.feylabs.halalkan.utils

import com.feylabs.halalkan.data.remote.reqres.masjid.DataMasjid
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse


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

        fun getMasjidDataPaginationResponsePlaceHolder() = DataMasjid(
            id = 0,
            createdAt = "",
            updatedAt = "",
            address ="",
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
            ViewType = VFooter
        )
    }
}
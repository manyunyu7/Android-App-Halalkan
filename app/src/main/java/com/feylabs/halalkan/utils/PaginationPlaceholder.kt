package com.feylabs.halalkan.utils

import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse



class PaginationPlaceholder {
    companion object{
        const val VNormal = 2
        const val VFooter = 3
        fun getMasjidReviewPaginationResponsePlaceHolder() = MasjidReviewPaginationResponse.Reviews.Data(
            id = 0,
            masjidId = 0,
            ratingId = 0,
            reviewCategory = "",
            comment = "",
            createdAt ="",
            updatedAt ="",
            reviewPhotos = mutableListOf(),
            userId = 0,
            userInfo = null,
            ViewType = VFooter
        )
    }
}
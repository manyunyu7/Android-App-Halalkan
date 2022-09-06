package com.feylabs.halalkan.data.remote.reqres.resto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
data class RestoDetailResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {
    @Keep
    data class Data(
        @SerializedName("detailResto")
        var detailResto: RestoModelResponse,
        @SerializedName("photos")
        var photos: List<String> = listOf(),
        @SerializedName("rating")
        var rating: Double = 0.0,
        @SerializedName("totalRating")
        var totalRating: Double = 0.0,
        @SerializedName("totalReview")
        var totalReview: Double = 0.0
    ) {

    }
}
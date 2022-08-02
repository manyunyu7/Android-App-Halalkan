package com.feylabs.halalkan.data.remote.reqres.masjid


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MasjidResponseWithoutPagination(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<MasjidModelResponse>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
) : Parcelable {
}



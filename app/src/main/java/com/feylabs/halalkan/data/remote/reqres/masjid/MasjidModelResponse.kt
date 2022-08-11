package com.feylabs.halalkan.data.remote.reqres.masjid
import android.os.Parcelable
import androidx.annotation.Nullable
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MasjidModelResponse(
    @SerializedName("address")
    val address: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("facilities")
    val facilities: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("img")
    val img: String,
    @SerializedName("img_full_path")
    val img_full_path: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("long")
    val long: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("operating_end")
    val operatingEnd: String,
    @SerializedName("operating_start")
    val operatingStart: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("type_id")
    val typeId: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("review_avg")
    val review_avg: String,
    @SerializedName("review_count")
    val review_count: String,
    var distanceKm: String? = null,
    var distanceKmDoubleRounded: Double? = null,
    var distanceKmDouble: Double? = null,
    @Nullable
    val ViewType: Int = PaginationPlaceholder.VNormal,
    var isFooterVisible: Boolean = true,
) : Parcelable {
    fun getOperatingHours() = operatingStart.replace("T", "") + "-" + operatingEnd.replace("T", "")
}
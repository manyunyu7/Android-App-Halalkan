package com.feylabs.halalkan.data.remote.reqres.resto

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.Nullable
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RestoModelResponse(
    @SerializedName("address")
    var address: String = "",
    @SerializedName("certification_id")
    var certificationId: Int = 0,
    @SerializedName("certification_name")
    var certificationName: String = "",
    @SerializedName("food_type_name")
    var foodTypeName: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("image")
    var image: String = "",
    @SerializedName("is_visible")
    var isVisible: Int = 0,
    @SerializedName("lat")
    var lat: String = "",
    @SerializedName("long")
    var long: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("phone_number")
    var phoneNumber: String = "",
    @SerializedName("type_food_id")
    var typeFoodId: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("img_full_path")
    var image_full_path: String = "",
    @SerializedName("user_id")
    var userId: Int = 0,
    var distanceKm: String? = null,
    var distanceKmDoubleRounded: Double? = null,
    var distanceKmDouble: Double? = null,
    @Nullable
    val ViewType: Int = PaginationPlaceholder.VNormal,
    var isFooterVisible: Boolean = true,
) : Parcelable {

    fun getOperatingHours() : String = "-"

}
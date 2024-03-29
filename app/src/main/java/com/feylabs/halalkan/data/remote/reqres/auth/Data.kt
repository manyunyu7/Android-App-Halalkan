package com.feylabs.halalkan.data.remote.reqres.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("photo_path")
    val photo_path: String? = "",
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("roles_id")
    val rolesId: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("img_full_path")
    val imgFullPath: String,
    @SerializedName("driver_order_count")
    val isDriverAvailable: Int = -99,
) : Parcelable {

    fun isDriverAvailable(): Boolean {
        return isDriverAvailable == 0
    }

    fun getPhotoPath(): String {
        if (photo_path != null) {
            return photo_path
        } else {
            return "https://setkab.go.id/wp-content/uploads/2022/04/WhatsApp-Image-2022-04-29-at-17.22.03.jpeg"
        }
    }
}
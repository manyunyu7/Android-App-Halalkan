package com.feylabs.halalkan.data.remote.reqres.driver

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class DriverObj(
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("is_available")
    var isAvailable: Int = 0,
    @SerializedName("is_driver_have_order")
    var isDriverHaveOrder: Boolean = false,
    @SerializedName("lat")
    var lat: String = "",
    @SerializedName("long")
    var long: String = "",
    @SerializedName("resto_id")
    var restoId: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("user_driver")
    var userDriver: UserDriver = UserDriver(),
    @SerializedName("user_id")
    var userId: Int = 0
) : Parcelable {
    @Keep
    @Parcelize
    data class UserDriver(
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("email")
        var email: String = "",
        @SerializedName("email_verified_at")
        var emailVerifiedAt: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("img_full_path")
        var imgFullPath: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("phone_number")
        var phoneNumber: String = "",
        @SerializedName("photo")
        var photo: String = "",
        @SerializedName("roles_id")
        var rolesId: Int = 0,
        @SerializedName("updated_at")
        var updatedAt: String = ""
    ) : Parcelable
}

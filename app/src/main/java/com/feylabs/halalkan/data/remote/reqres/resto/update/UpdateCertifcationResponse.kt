package com.feylabs.halalkan.data.remote.reqres.resto.update


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UpdateCertificationResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {
    @Keep
    data class Data(
        @SerializedName("address")
        var address: String = "",
        @SerializedName("certification_id")
        var certificationId: String = "",
        @SerializedName("certification_name")
        var certificationName: String = "",
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("image")
        var image: String = "",
        @SerializedName("img_full_path")
        var imgFullPath: String = "",
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
        @SerializedName("user_id")
        var userId: Int = 0
    )
}
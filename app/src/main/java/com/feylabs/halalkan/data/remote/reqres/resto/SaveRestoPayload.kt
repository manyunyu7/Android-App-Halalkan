package com.feylabs.halalkan.data.remote.reqres.resto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class SaveRestoPayload(
    @SerializedName("address")
    var address: String = "",
    @SerializedName("certification_id")
    var certificationId: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("image")
    var image: Image = Image(),
    @SerializedName("is_visible")
    var isVisible: String = "",
    @SerializedName("lat")
    var lat: String = "",
    @SerializedName("long")
    var long: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("phone_number")
    var phoneNumber: String = "",
    @SerializedName("type_food_id")
    var typeFoodId: String = ""
) {
    @Keep
    class Image
}
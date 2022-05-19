package com.feylabs.halalkan.data.remote.reqres.masjid


import com.google.gson.annotations.SerializedName

data class MasjidResponseWithoutPagination(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
) {
    data class Data(
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
        val updatedAt: String
    )
}
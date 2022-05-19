package com.feylabs.halalkan.data.remote.reqres


import com.google.gson.annotations.SerializedName
import com.kemendag.ppid.ui.example.ExamplePaginationAdapter

data class MenteeResponse(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("from")
    val from: Int,
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("next_page_url")
    val nextPageUrl: String,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("prev_page_url")
    val prevPageUrl: Any,
    @SerializedName("to")
    val to: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("fakultas")
        val fakultas: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("jk")
        val jk: Int,
        @SerializedName("kelas")
        val kelas: String,
        @SerializedName("kelompok_id")
        val kelompokId: Int,
        @SerializedName("line_id")
        val lineId: Any,
        @SerializedName("nama")
        val nama: String,
        @SerializedName("nim")
        val nim: String,
        @SerializedName("no_telp")
        val noTelp: Any,
        @SerializedName("program_studi")
        val programStudi: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        val ViewType: Int = ExamplePaginationAdapter.Normal,
        var isVisible: Boolean = true,
    )
}
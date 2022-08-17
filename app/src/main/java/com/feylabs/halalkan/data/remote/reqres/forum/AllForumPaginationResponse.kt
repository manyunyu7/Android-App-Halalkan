package com.feylabs.halalkan.data.remote.reqres.forum


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.annotation.Nullable
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.utils.PaginationPlaceholder

@Keep
data class AllForumPaginationResponse(
    @SerializedName("current_page")
    var currentPage: Int = 0,
    @SerializedName("data")
    var `data`: List<ForumModelResponse> = listOf(),
    @SerializedName("first_page_url")
    var firstPageUrl: String = "",
    @SerializedName("from")
    var from: Int = 0,
    @SerializedName("last_page")
    var lastPage: Int = 0,
    @SerializedName("last_page_url")
    var lastPageUrl: String = "",
    @SerializedName("links")
    var links: List<Link> = listOf(),
    @SerializedName("next_page_url")
    var nextPageUrl: Any? = Any(),
    @SerializedName("path")
    var path: String = "",
    @SerializedName("per_page")
    var perPage: String = "",
    @SerializedName("prev_page_url")
    var prevPageUrl: Any? = Any(),
    @SerializedName("to")
    var to: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) {


    @Keep
    data class Link(
        @SerializedName("active")
        var active: Boolean = false,
        @SerializedName("label")
        var label: String = "",
        @SerializedName("url")
        var url: String? = ""
    )
}
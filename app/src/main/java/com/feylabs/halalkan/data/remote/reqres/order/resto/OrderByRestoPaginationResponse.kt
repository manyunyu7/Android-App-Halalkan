package com.feylabs.halalkan.data.remote.reqres.order.resto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryModel

@Keep
data class OrderByRestoPaginationResponse(
    @SerializedName("current_page")
    var currentPage: Int = 0,
    @SerializedName("data")
    var resto: List<OrderHistoryModel> = listOf(),
    @SerializedName("first_page_url")
    var firstPageUrl: String = "",
    @SerializedName("from")
    var from: Int = 0,
    @SerializedName("last_page")
    var lastPage: Int = 0,
    @SerializedName("last_page_url")
    var lastPageUrl: String = "",
    @SerializedName("next_page_url")
    var nextPageUrl: String = "",
    @SerializedName("path")
    var path: String = "",
    @SerializedName("per_page")
    var perPage: String = "",
    @SerializedName("prev_page_url")
    var prevPageUrl: String = "",
    @SerializedName("to")
    var to: Int = 0,
    @SerializedName("total")
    var total: Int = 0
) {

}
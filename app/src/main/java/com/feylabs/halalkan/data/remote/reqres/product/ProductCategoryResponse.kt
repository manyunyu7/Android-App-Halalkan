package com.feylabs.halalkan.data.remote.reqres.product


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

class ProductCategoryResponse : ArrayList<ProductCategoryResponse.ProductCategoryResponseItem>(){
    @Keep
    data class ProductCategoryResponseItem(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("image")
        var image: String = ""
    )
}
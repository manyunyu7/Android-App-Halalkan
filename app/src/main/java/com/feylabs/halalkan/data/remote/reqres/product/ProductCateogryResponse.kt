package com.feylabs.halalkan.data.remote.reqres.product


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.feylabs.halalkan.utils.Network

class ProductCateogryResponse : ArrayList<ProductCateogryResponse.ProductCateogryResponseItem>(){
    @Keep
    data class ProductCateogryResponseItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    ){
        fun getImage(): String? {
            return Network.FEYLABS_CDN +"product_category/$name.png"
        }
    }
}
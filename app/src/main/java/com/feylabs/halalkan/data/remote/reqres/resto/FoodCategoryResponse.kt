package com.feylabs.halalkan.data.remote.reqres.resto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.feylabs.halalkan.data.remote.reqres.resto.FoodCategoryResponse.FoodCategoryResponseItem

class FoodCategoryResponse : ArrayList<FoodCategoryResponseItem>(){
    @Keep
    data class FoodCategoryResponseItem(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("resto_id")
        var restoId: Int = 0,
        var isActive: Boolean = false,
    )
}
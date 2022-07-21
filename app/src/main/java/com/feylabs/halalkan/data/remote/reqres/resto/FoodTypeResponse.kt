package com.feylabs.halalkan.data.remote.reqres.resto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

class FoodTypeResponse : ArrayList<FoodTypeResponse.FoodTypeResponseItem>() {
    @Keep
    data class FoodTypeResponseItem(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = ""
    ) {
        fun getImage(): String? {
            return ""
        }
    }
}
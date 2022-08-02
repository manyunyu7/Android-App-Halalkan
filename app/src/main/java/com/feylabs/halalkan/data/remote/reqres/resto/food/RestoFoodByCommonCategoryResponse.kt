package com.feylabs.halalkan.data.remote.reqres.resto.food


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RestoFoodByCommonCategoryResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: List<FoodModelResponse> = listOf(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {

}
package com.feylabs.halalkan.data.remote.reqres.product

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProductModelResponse(
    @SerializedName("category_id")
    var categoryId: Int = 0,
    @SerializedName("category_name")
    var categoryName: String = "",
    @SerializedName("certification_id")
    var certificationId: Int = 0,
    @SerializedName("certification_name")
    var certificationName: String = "",
    @SerializedName("code")
    var code: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("img")
    var img: String = "",
    @SerializedName("img_full_path")
    var imgFullPath: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("product_information")
    var productInformation: ProductInformation? = ProductInformation(),
    @SerializedName("updated_at")
    var updatedAt: String = ""
) {
    @Keep
    data class ProductInformation(
        @SerializedName("allergy")
        var allergy: List<String> = listOf(),
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("environment")
        var environment: List<String> = listOf(),
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("ingredient")
        var ingredient: List<String> = listOf(),
        @SerializedName("product_id")
        var productId: Int = 0,
        @SerializedName("summary")
        var summary: List<String> = listOf(),
        @SerializedName("updated_at")
        var updatedAt: String = ""
    )
}

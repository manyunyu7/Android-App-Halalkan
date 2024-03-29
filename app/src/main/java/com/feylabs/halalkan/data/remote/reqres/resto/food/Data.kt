package com.feylabs.halalkan.data.remote.reqres.resto.food

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.DecimalFormat
import java.text.NumberFormat

@Keep
@Parcelize
data class FoodModelResponse(
    @SerializedName("category_id")
    var categoryId: Int = 0,
    @SerializedName("category_name")
    var categoryName: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("image")
    var image: String = "",
    @SerializedName("img_full_path")
    var imgFullPath: String = "",
    @SerializedName("is_visible")
    var isVisible: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("price")
    var price: Double = 0.0,
    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("restoran_id")
    var restoranId: Int = 0,
    @SerializedName("type_food_id")
    var typeFoodId: Int = 0,
    @SerializedName("type_food_name")
    var typeFoodName: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    var isOrdered: Boolean = false,
    var orderedQuantity: Int = 0,
    var notes: String = "",
) : Parcelable {
    fun getFormattedPrice(): String {
        val formatter: NumberFormat = DecimalFormat("#,###")
        val myNumber = this.price
        val formattedNumber: String = formatter.format(myNumber)
        return formattedNumber + " won"
    }
}
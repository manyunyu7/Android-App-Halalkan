package com.feylabs.halalkan.view.products

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryProductsModel (
    val img_category: Int,
    val product_category: String
) : Parcelable
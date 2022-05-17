package com.feylabs.halalkan.view.new_home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantHomeUIModel(
    val categoryTop: String, //Muslim Friendly, etc
    val categoryMiddle: String, //General Restaurant
    val distance: String, // Distance
    val title: String, //Title
    val address: String, //address
    val cuisineCategory: String, // cuisine category
    val star: String, // cuisine category
    val id: String, // cuisine category
    val image: String, // cuisine category
) : Parcelable
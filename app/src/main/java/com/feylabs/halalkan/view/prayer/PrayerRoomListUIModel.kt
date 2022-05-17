package com.feylabs.halalkan.view.prayer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrayerRoomListUIModel(
    val categoryTop: String, //Muslim Friendly, etc
    val categoryMiddle: String, //General Restaurant
    val distance: String, // Distance
    val title: String, //Title
    val address: String, //address
    val star: String, // cuisine category
    val id: String, // cuisine category
    val image: String, // cuisine category
    val rating: String, // cuisine category,
    val createdAt: String = ""
) : Parcelable
package com.feylabs.halalkan.data.remote.reqres.driver


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UpdateLocationPayload(
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("long")
    var long: Double,
)
package com.feylabs.halalkan.data.remote.reqres.masjid


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

class MasjidTypeResponse : ArrayList<MasjidTypeResponse.MasjidTypeResponseItem>(){
    @Keep
    data class MasjidTypeResponseItem(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = ""
    )
}
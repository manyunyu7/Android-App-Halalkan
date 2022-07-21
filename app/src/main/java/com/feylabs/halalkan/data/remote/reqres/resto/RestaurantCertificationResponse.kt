package com.feylabs.halalkan.data.remote.reqres.resto


import com.google.gson.annotations.SerializedName

class RestaurantCertificationResponse : ArrayList<RestaurantCertificationResponse.RestaurantCertificationItem>(){
    data class RestaurantCertificationItem(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = ""
    ){
        fun getImage():String{
            return ""
        }
    }
}
package com.feylabs.halalkan.data.remote.reqres.driver


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel

@Keep
data class GetAllDriverResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: List<Data> = listOf(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {
    fun getDrivers() : MutableList<UserModel>{
        val tempList = mutableListOf<UserModel>()

        data.forEachIndexed { index, data ->
            data.userData?.let {
                tempList.add(it)
            }
        }

        return tempList
    }


    @Keep
    data class Data(
        @SerializedName("driverId")
        var driverId: Int = 0,
        @SerializedName("userData")
        var userData: UserModel? = null
    ) {

    }
}
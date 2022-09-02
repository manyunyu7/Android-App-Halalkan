package com.feylabs.halalkan.data.remote.reqres.resto.operating_hour


import android.content.Context
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.feylabs.halalkan.R

@Keep
data class RestoOperatingHourResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: List<Data> = listOf(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {
    @Keep
    data class Data(
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("day")
        var day: String = "",
        @SerializedName("day_code")
        var dayCode: String = "",
        @SerializedName("hour")
        var hour: String = "",
        @SerializedName("hour_end")
        var hourEnd: String = "",
        @SerializedName("hour_start")
        var hourStart: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("restorans_id")
        var restoransId: Int = 0,
        @SerializedName("updated_at")
        var updatedAt: String = ""
    ){
        fun translateDay(context: Context): String {
            var day = "-"
            when(dayCode){
                "1"-> day=context.getString(R.string.day_monday)
                "2"-> day=context.getString(R.string.day_tuesday)
                "3"-> day=context.getString(R.string.day_wednesday)
                "4"-> day=context.getString(R.string.day_thursday)
                "5"-> day=context.getString(R.string.day_friday)
                "6"-> day=context.getString(R.string.day_saturday)
                "7"-> day=context.getString(R.string.day_sunday)
            }
            return day
        }
    }
}
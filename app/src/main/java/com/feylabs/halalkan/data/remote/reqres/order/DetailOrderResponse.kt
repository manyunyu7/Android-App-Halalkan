package com.feylabs.halalkan.data.remote.reqres.order


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.data.remote.reqres.driver.DriverObj
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import kotlinx.parcelize.Parcelize
import java.text.DecimalFormat
import java.text.NumberFormat

@Keep
data class DetailOrderResponse(
    @SerializedName("code")
    var code: Int = 0,
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {


    @Keep
    @Parcelize
    data class Data(
        @SerializedName("address")
        var address: String = "",
        @SerializedName("created_at")
        var createdAt: String = "",
        @SerializedName("driver_id")
        var driverId: Int? = 0,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("lat")
        var lat: String = "",
        @SerializedName("long")
        var long: String = "",
        @SerializedName("orders")
        var orders: List<OrderDataModel>?= listOf(),
        @SerializedName("reject_reason")
        var rejectReason: String? = "",
        @SerializedName("resto_id")
        var restoId: Int = 0,
        @SerializedName("resto_obj")
        var restoObj: RestoModelResponse? = null,
        @SerializedName("status_desc")
        var statusDesc: String = "",
        @SerializedName("status_id")
        var statusId: Int = 0,
        @SerializedName("total_price")
        var totalPrice: Int = 0,
        @SerializedName("updated_at")
        var updatedAt: String = "",
        @SerializedName("user_id")
        var userId: Int = 0,
        @SerializedName("user_obj")
        var userObj: UserModel? = null,
        @SerializedName("driver_obj")
        var driverObj: DriverObj? = null,
        @SerializedName("user_sign")
        var userSign: String = "",
        @SerializedName("img_signature_full_path")
        var imgSignatureFullPath: String = ""
    ) : Parcelable {
        fun getTotalItem() : Int{
            return orders?.size ?: 0
        }

        fun getFormattedTotalPrice(): String {
            val formatter: NumberFormat = DecimalFormat("#,###")
            val myNumber = this.totalPrice
            val formattedNumber: String = formatter.format(myNumber)
            return "$formattedNumber won"
        }

    }
}
package com.feylabs.halalkan.utils.masjid

import com.feylabs.halalkan.data.remote.reqres.masjid.DataMasjid
import com.feylabs.halalkan.utils.NumberUtil.Companion.round
import com.feylabs.halalkan.utils.NumberUtil.Companion.roundOffDecimal
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong

object MasjidUtility {

    fun MutableList<DataMasjid>.renderWithDistanceModel(
        myLocation: MyLatLong,
        limit: Int = 100,
        limitDistance: Int = 100,
    ): MutableList<DataMasjid> {

        val resultList = mutableListOf<DataMasjid>()

        this.forEachIndexed { index, dataMasjid ->
            val distance = LocationUtils.calculateDistance(
                loc1 = myLocation,
                loc2 = MyLatLong(dataMasjid.lat.toDouble(), dataMasjid.long.toDouble())
            )
            dataMasjid.distanceKmDouble = distance
            dataMasjid.distanceKm = distance.roundOffDecimal()
            resultList.add(dataMasjid)
        }

        return resultList
    }


}
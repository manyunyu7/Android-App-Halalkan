package com.feylabs.halalkan.utils.resto

import android.graphics.Color
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidModelResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import com.feylabs.halalkan.utils.NumberUtil.Companion.roundOffDecimal
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong

object RestoUtility {

    fun MutableList<RestoModelResponse>.renderWithDistanceModel(
        myLocation: MyLatLong,
        limit: Int = 100,
        limitDistance: Int = 20000,
        sortByNearest: Boolean = true
    ): MutableList<RestoModelResponse> {
        val resultList = mutableListOf<RestoModelResponse>()

        this.forEachIndexed { index, dataResto ->
            val distance = LocationUtils.calculateDistance(
                loc1 = myLocation,
                loc2 = MyLatLong(dataResto.lat.toDouble(), dataResto.long.toDouble())
            )
            dataResto.distanceKmDouble = distance
            dataResto.distanceKm = distance.roundOffDecimal()
            resultList.add(dataResto)
        }

        var tempResult = resultList

        //sort by nearest
        if (sortByNearest) {
            tempResult.sortBy { it.distanceKmDouble }
        }

        // add limitation
        tempResult = tempResult.take(limit).toMutableList()

        tempResult = tempResult.filter { dataMasjid ->
            dataMasjid.distanceKmDouble?.let {
                it < limitDistance.toDouble()
            } == true
        }.toMutableList()



        return tempResult
    }

    fun Int.getStatusColor(): Int {

        var color = "#F59F00"

        if (this == 1) {
            color = "#F59F00"
        }

        if (this == 2) {
            color = "#156DBE"
        }
        if (this == 3) {
            color = "#000000"
        }

        if (this == 4) {
            color = "#008D36"
        }

        if (this == 5) {
            color = "#DC0202"
        }

        return Color.parseColor(color)

    }

    private fun Double?.orZero(): Double {
        if (this == null) {
            return 0.0
        } else
            return this
    }

}


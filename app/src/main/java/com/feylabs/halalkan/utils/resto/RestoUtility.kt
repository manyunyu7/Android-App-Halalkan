package com.feylabs.halalkan.utils.resto

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

        tempResult = tempResult.filter {dataMasjid->
            dataMasjid.distanceKmDouble?.let {
                it < limitDistance.toDouble()
            } == true
        }.toMutableList()



        return tempResult
    }

    private fun Double?.orZero(): Double {
        if(this==null){
            return 0.0
        }else
            return this
    }

}


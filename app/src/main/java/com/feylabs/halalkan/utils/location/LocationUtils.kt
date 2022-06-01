package com.feylabs.halalkan.utils.location

import androidx.lifecycle.MutableLiveData

class LocationUtils {
    companion object{
        fun calculateDistance(loc1: MyLatLong?, loc2: MyLatLong?): Double {
            val mLoc2 = loc2 ?: MyLatLong(-99.0,-22.0)
            val mLoc1 = loc1 ?: MyLatLong(-99.0,-22.0)

            val lat1 = mLoc1.lat
            val lon1 = mLoc1.long

            val lat2 = mLoc2.lat
            val lon2 = mLoc2.long

            val theta = lon1 - lon2
            var dist = (Math.sin(deg2rad(lat1))
                    * Math.sin(deg2rad(lat2))
                    + (Math.cos(deg2rad(lat1))
                    * Math.cos(deg2rad(lat2))
                    * Math.cos(deg2rad(theta))))
            dist = Math.acos(dist)
            dist = rad2deg(dist)
            dist = dist * 60 * 1.1515
            return dist.toKm()
        }

        fun Float.toKm() = this * 1.60934
        fun Float.toMiles() = this / 1.60934

        fun Double.toKm() : Double = this * 1.60934
        fun Double.toMiles() = this / 1.60934

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }

        /**
         * Check if location set
         */
        fun checkIfLocationSet(liveLatLng: MyLatLong?): Boolean {
            val pairsValue = liveLatLng

            val lat =  pairsValue?.lat ?: -99.0
            val long =  pairsValue?.long ?: -99.0

            return !(lat==-99.0 && long == -99.0)
        }

    }
}




package com.feylabs.halalkan.utils.location

data class MyLatLong(
            val lat: Double,
            val long: Double
        ) {
            fun getLatString(): String {
                return lat.toString()
            }

            fun getLongString() = long.toString()
        }
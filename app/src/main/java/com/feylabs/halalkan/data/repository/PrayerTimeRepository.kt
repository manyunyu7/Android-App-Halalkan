package com.feylabs.halalkan.data.repository

import com.feylabs.halalkan.data.remote.RemoteDataSource

class PrayerTimeRepository(
    private val remoteDs: RemoteDataSource,
) {
    suspend fun getPrayerTimeSingleDate(
        time: String,
        lat: String,
        long: String,
        method: String
    ) = remoteDs.getPrayerTimeSingleDate(
        time = time,
        lat = lat,
        long = long,
        method = method
    )
}
package com.feylabs.halalkan.data

import com.feylabs.halalkan.data.remote.RemoteDataSource

class MasjidRepository(
    private val remoteDs: RemoteDataSource,
) {

    suspend fun getAllMasjid() = remoteDs.getAllMasjid()
}
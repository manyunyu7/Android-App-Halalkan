package com.feylabs.halalkan.data

import com.feylabs.halalkan.data.remote.RemoteDataSource

class MasjidRepository(
    private val remoteDs: RemoteDataSource,
) {

    suspend fun getAllMasjid() = remoteDs.getAllMasjid()
    suspend fun getMasjidPhotos(id: String) = remoteDs.getMasjidPhotos(id)
    suspend fun getMasjidDetail(id: String) = remoteDs.getMasjidDetail(id)
}
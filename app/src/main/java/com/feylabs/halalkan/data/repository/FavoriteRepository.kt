package com.feylabs.halalkan.data.repository

import com.feylabs.halalkan.data.remote.RemoteDataSource

class FavoriteRepository(
    private val remoteDs: RemoteDataSource,
) {

    suspend fun getAllFavorite() = remoteDs.getAllMasjid()
    suspend fun addFavoriteMasjid(masjidId: String) = remoteDs.addFavoriteMasjid(masjidId)
    suspend fun addFavoriteResto(restoId: String) = remoteDs.addFavoriteResto(restoId)
}
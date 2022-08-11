package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavMasjidResponse
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavRestoResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.*
import retrofit2.Response
import retrofit2.http.*


interface FavoriteService {

    @GET("favorites")
    suspend fun getAllFavorites(
    ): Response<MasjidResponseWithoutPagination>

    @POST("favorites/addResto/{restoId}")
    suspend fun addFavoriteResto(
        @Path("restoId") restoId: String,
    ): Response<AddFavRestoResponse>

    @POST("favorites/addMasjid/{masjidId}")
    suspend fun addFavoriteMasjid(
        @Path("masjidId") masjidId: String,
    ): Response<AddFavMasjidResponse>

}
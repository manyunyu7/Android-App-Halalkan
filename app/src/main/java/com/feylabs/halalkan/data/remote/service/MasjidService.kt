package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.*
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidDetailResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface MasjidService {

    @GET("masjids/showAll")
    suspend fun showAllMasjid(
    ): Response<MasjidResponseWithoutPagination>

    @GET("masjids/{id}/photos")
    suspend fun getMasjidPhotos(
        @Path("id") masjidId: String,
    ): Response<MasjidPhotosResponse>

    @GET("masjids/{id}")
    suspend fun getMasjidDetail(
        @Path("id") masjidId: String,
    ): Response<MasjidDetailResponse>

}
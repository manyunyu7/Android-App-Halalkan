package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.*
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface MasjidService {

    @GET("masjids/showAll")
    suspend fun showAllMasjid(
    ): Response<MasjidResponseWithoutPagination>

    @GET("masjids/{postId}/photos")
    suspend fun getMasjidPhotos(
    ): Response<MasjidPhotosResponse>

    @GET("masjids/{postId}/photos")
    suspend fun getMasjidDetail(
    ): Response<MasjidPhotosResponse>

}
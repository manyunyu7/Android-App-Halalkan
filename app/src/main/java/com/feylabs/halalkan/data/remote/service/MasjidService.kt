package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.*
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface MasjidService {

    @GET("/php-translator-api")
    suspend fun translate(
        @Query("source") langSource: String,
        @Query("target") target: Target,
        @Query("text") text: String,
    ): Response<MasjidResponseWithoutPagination>

}
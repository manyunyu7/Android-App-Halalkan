package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.translator.TiktokTextToSpeechResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TranslateResponse
import retrofit2.Response
import retrofit2.http.*


interface TranslatorService {

    @GET("php-translator")
    suspend fun translate(
        @Query("source") langSource: String,
        @Query("target") target: String,
        @Query("text") text: String,
    ): Response<TranslateResponse>

    @GET("tiktok-tts")
    suspend fun getTTS(
        @Query("lang") lang: String,
        @Query("text") text: String,
    ): Response<TiktokTextToSpeechResponse>

}
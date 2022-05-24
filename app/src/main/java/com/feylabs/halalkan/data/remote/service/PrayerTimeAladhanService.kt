package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TiktokTextToSpeechResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TranslateResponse
import retrofit2.Response
import retrofit2.http.*


interface PrayerTimeAladhanService {

    @GET("calendar/{time}")
    suspend fun getSinglePrayerTime(
        @Path("time") timeunix:String,
        @Query("source") latitude: String,
        @Query("target") longitude: String,
        @Query("method") method: String="11",
    ): Response<PrayerTimeAladhanSingleDateResponse>


}
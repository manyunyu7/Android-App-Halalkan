package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TiktokTextToSpeechResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TranslateResponse
import retrofit2.Response
import retrofit2.http.*


interface PrayerTimeAladhanService {

    @GET()
    suspend fun getSinglePrayerTime(
        @Url() fullUrl:String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("method") method: String="11",
    ): Response<PrayerTimeAladhanSingleDateResponse>


}
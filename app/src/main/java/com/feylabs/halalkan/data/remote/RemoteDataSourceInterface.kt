package com.feylabs.halalkan.data.remote

import com.feylabs.halalkan.data.remote.reqres.*
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.LoginResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidDetailResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TiktokTextToSpeechResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TranslateResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface RemoteDataSourceInterface {

    suspend fun getPrayerTimeSingleDate(
        time: String,
        lat: String,
        long: String,
        method: String
    ): Response<PrayerTimeAladhanSingleDateResponse>

    suspend fun getMasjidDetail(id: String): Response<MasjidDetailResponse>
    suspend fun getMasjidPhotos(id: String): Response<MasjidPhotosResponse>
    suspend fun getAllMasjid(): Response<MasjidResponseWithoutPagination>

    suspend fun getTextToSpeech(
        langSource: String, text: String
    ): Response<TiktokTextToSpeechResponse>

    suspend fun getTranslation(
        langSource: String, target: String, text: String
    ): Response<TranslateResponse>

    suspend fun register(bodyRequest: RegisterBodyRequest) : Response<RegisterResponse>

    suspend fun login(loginBodyRequest: LoginBodyRequest) : Response<LoginResponse>

    suspend fun getMasjids() : Response<MasjidResponseWithoutPagination>

}
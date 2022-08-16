package com.feylabs.halalkan.data.remote

import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.LoginResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavMasjidResponse
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavRestoResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.*
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateCertificationResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TiktokTextToSpeechResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TranslateResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response


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

    suspend fun register(bodyRequest: RegisterBodyRequest): Response<RegisterResponse>

    suspend fun login(loginBodyRequest: LoginBodyRequest): Response<LoginResponse>

    suspend fun getMasjids(): Response<MasjidResponseWithoutPagination>
    suspend fun getMasjidsPagination(page:Int): Response<AllMasjidPaginationResponse>
    suspend fun getMasjidReviews(
        masjidId: String,
        perPage: Int,
        page: Int
    ): Response<MasjidReviewPaginationResponse>

    suspend fun createMasjidReview(masjidId: String,body:RequestBody) : Response<ResponseBody?>?



    suspend fun getRestoCert() : Response<RestaurantCertificationResponse>
    suspend fun getRestoAll() : Response<AllRestoNoPagination>
    suspend fun getFoodType() : Response<FoodTypeResponse>
    suspend fun getRestoDetail(id:String) : Response<RestoDetailResponse>
    suspend fun getRestoFoodByCommonCategory(restoId:String,categoryId:String) : Response<AllFoodByRestoResponse>
    suspend fun createRestoReview(restoId: String,body:RequestBody) : Response<ResponseBody?>?
    suspend fun getRestoReviews(
        restoId: String,
        perPage: Int,
        page: Int
    ): Response<RestoReviewPaginationResponse>

    suspend fun createResto(body:RequestBody) : Response<ResponseBody?>?
    suspend fun createRestoFoodCategory(id:String,body:RequestBody) : Response<ResponseBody?>?
    suspend fun updateCertication(id:String,body:RequestBody) : Response<UpdateCertificationResponse?>?



    suspend fun getFoodCategoryOnResto(id:String) : Response<FoodCategoryResponse>
    suspend fun getFoodByCategory(id:String) : Response<AllFoodByRestoResponse>
    suspend fun getAllFoodByResto(id:String) : Response<AllFoodByRestoResponse>

    suspend fun getMyResto() : Response<AllRestoNoPagination>

    suspend fun addFavoriteMasjid(masjidId:String) : Response<AddFavMasjidResponse>
    suspend fun addFavoriteResto(restoId:String) : Response<AddFavRestoResponse>
}
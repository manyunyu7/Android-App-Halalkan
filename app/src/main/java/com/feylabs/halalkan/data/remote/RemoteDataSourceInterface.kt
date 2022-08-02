package com.feylabs.halalkan.data.remote

import com.feylabs.halalkan.data.remote.reqres.*
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.LoginResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.*
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.reqres.product.ProductCateogryResponse
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.FoodTypeResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestaurantCertificationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.data.remote.reqres.resto.food.RestoFoodByCommonCategoryResponse
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

    suspend fun register(bodyRequest: RegisterBodyRequest): Response<RegisterResponse>

    suspend fun login(loginBodyRequest: LoginBodyRequest): Response<LoginResponse>

    suspend fun getMasjids(): Response<MasjidResponseWithoutPagination>

    suspend fun getMasjidsPagination(page:Int): Response<AllMasjidPaginationResponse>

    suspend fun getMasjidReviews(
        masjidId: String,
        perPage: Int,
        page: Int
    ): Response<MasjidReviewPaginationResponse>



    suspend fun getRestoCert() : Response<RestaurantCertificationResponse>
    suspend fun getRestoAll() : Response<AllRestoNoPagination>
    suspend fun getFoodType() : Response<FoodTypeResponse>
    suspend fun getRestoDetail(id:String) : Response<RestoDetailResponse>
    suspend fun getRestoFoodByCommonCategory(restoId:String,categoryId:String) : Response<RestoFoodByCommonCategoryResponse>



    suspend fun getProductCategory() : Response<ProductCateogryResponse>

}
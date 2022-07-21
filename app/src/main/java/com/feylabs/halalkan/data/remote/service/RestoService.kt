package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.*
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.LoginResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.FoodTypeResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestaurantCertificationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import retrofit2.Response
import retrofit2.http.*


interface RestoService {


    @GET("/users")
    suspend fun getUsers(): Response<UserResponse>

    @GET("fe/restoran/cert")
    suspend fun getCert(): Response<RestaurantCertificationResponse>

    @GET("fe/restoran/food-type")
    suspend fun getFoodType(): Response<FoodTypeResponse>

    @GET("fe/restoran/{id}/detail")
    suspend fun getDetail(
        @Path("id") id: String
    ): Response<RestoDetailResponse>

    @GET("restoran/all")
    suspend fun getAllRaw(): Response<AllRestoNoPagination>

    @GET("fe/restoran/all-raw")
    suspend fun getAll(): Response<RestaurantCertificationResponse>

    @GET("fe/restoran/nearby")
    suspend fun getNearby(): Response<RestaurantCertificationResponse>


}
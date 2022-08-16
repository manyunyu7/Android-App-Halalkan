package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateCertificationResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface RestoService {


    @GET("/users")
    suspend fun getUsers(): Response<UserResponse>

    @GET("fe/restoran/cert")
    suspend fun getCert(): Response<RestaurantCertificationResponse>

    @GET("fe/restoran/food-type")
    suspend fun getFoodType(): Response<FoodTypeResponse>

    //get food per resto category
    @GET("fe/restoran/food/category/{categoryId}")
    suspend fun getFoodByResto(
        @Path("categoryId") categoryId:String,
    ): Response<AllFoodByRestoResponse>

    @GET("fe/restoran/{id}/detail")
    suspend fun getDetail(
        @Path("id") id: String
    ): Response<RestoDetailResponse>

    @GET("fe/restoran/{id}/food-category")
    suspend fun getFoodCategoryOnResto(
        @Path("id") id: String
    ): Response<FoodCategoryResponse>

    @GET("fe/restoran/food/category/{id}")
    suspend fun getFoodByCategory(
        @Path("id") id: String
    ): Response<AllFoodByRestoResponse>

    @GET("fe/restoran/{restoId}/food")
    suspend fun getAllFoodOnResto(
        @Path("restoId") id: String
    ): Response<AllFoodByRestoResponse>

    @GET("restoran/all")
    suspend fun getAllRaw(): Response<AllRestoNoPagination>

    @GET("fe/restoran/all-raw")
    suspend fun getAll(): Response<RestaurantCertificationResponse>

    @GET("fe/restoran/nearby")
    suspend fun getNearby(): Response<RestaurantCertificationResponse>

    @GET("fe/restoran/{id}/reviews/")
    suspend fun getReviews(
        @Path("id") restoId: String,
        @Query("perPage") perPage: Int = 5,
        @Query("page") page: Int = 1
    ): Response<RestoReviewPaginationResponse>

    @POST("reviewResto/store/{id}")
    suspend fun createReview(
        @Body file: RequestBody?,
        @Path("id") restoId: String,
    ): Response<ResponseBody?>?

    @POST("restoran/store")
    suspend fun createResto(
        @Body file: RequestBody?,
    ): Response<ResponseBody?>?

    @POST("fe/restoran/{idResto}/update/cert")
    suspend fun updateCertification(
        @Body file: RequestBody?,
        @Path("idResto") restoId: String,
        ): Response<UpdateCertificationResponse?>?

    @POST("foods/category/createCategory/{id}")
    suspend fun createRestoFoodCategory(
        @Body file: RequestBody?,
        @Path("id") restoId: String,
        ): Response<ResponseBody?>?


    ///admin section
    @GET("fe/restoran/me")
    suspend fun getMyRestaurant(
    ): Response<AllRestoNoPagination>


}
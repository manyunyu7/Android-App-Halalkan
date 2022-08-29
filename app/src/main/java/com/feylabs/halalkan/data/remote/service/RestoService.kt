package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.LoginResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import com.feylabs.halalkan.data.remote.reqres.driver.GetAllDriverResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartPayload
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartResponse
import com.feylabs.halalkan.data.remote.reqres.order.DetailOrderResponse
import com.feylabs.halalkan.data.remote.reqres.order.OrderStatusResponse
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryResponse
import com.feylabs.halalkan.data.remote.reqres.order.resto.OrderByRestoPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateRestoColumnResponse
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
        @Path("categoryId") categoryId: String,
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

    @POST("fe/restoran/{idResto}/update/{urlupdate}")
    suspend fun updateRestoColumn(
        @Body file: RequestBody?,
        @Path("idResto") restoId: String,
        @Path("urlupdate") urlupdate: String,
    ): Response<UpdateRestoColumnResponse?>?


    @POST("foods/category/createCategory/{id}")
    suspend fun createRestoFoodCategory(
        @Body file: RequestBody?,
        @Path("id") restoId: String,
    ): Response<ResponseBody?>?

    ///user order
    @POST("fe/orders/carts/createCart/{restoId}")
    suspend fun createCart(
        @Body body: CreateCartPayload,
        @Path("restoId") restoId: String,
    ): Response<CreateCartResponse>


    @GET("orders/carts/myCarts")
    suspend fun getUserOrderHistory(
    ): Response<OrderHistoryResponse>


    ///admin section
    @GET("fe/restoran/me")
    suspend fun getMyRestaurant(
    ): Response<AllRestoNoPagination>

    @GET("fe/orders/carts/status")
    suspend fun getAllOrderStatus(
    ): Response<OrderStatusResponse>

    @FormUrlEncoded
    @POST("orders/carts/rejectOrder/{orderId}")
    suspend fun orderReject(
        @Field("rejected_reason") reason: String,
        @Path("orderId") orderId: String,
    ): Response<GeneralApiResponse>

    @POST("orders/carts/approvedOrder/{orderId}")
    suspend fun orderApprove(
        @Path("orderId") orderId: String,
    ): Response<GeneralApiResponse>

    @PUT("orders/carts/approvedDeliv/{orderId}")
    suspend fun orderDelivered(
        @Path("orderId") orderId: String,
        @Query("driver_id") driverId: Int
    ): Response<GeneralApiResponse>

    @GET("fe/restoran/{id}/getAllOrders")
    suspend fun getRestoOrder(
        @Path("id") restoId: String,
        @Query("perPage") perPage: Int = 5,
        @Query("page") page: Int = 1,
        @Query("status") status: String? = null
    ): Response<OrderByRestoPaginationResponse>


    @GET("orders/carts/detailOrder/{orderId}")
    suspend fun getOrderDetail(
        @Path("orderId") orderId: String,
    ): Response<DetailOrderResponse>

    @GET("driver/getByResto/{restoId}")
    suspend fun getAllDriverOnResto(
        @Path("restoId") orderId: String,
    ): Response<GetAllDriverResponse>

    @POST("driver/register")
    suspend fun addNewDriver(
        @Body body: RegisterBodyRequest
    ): Response<RegisterResponse>


}
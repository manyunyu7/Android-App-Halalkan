package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import com.feylabs.halalkan.data.remote.reqres.driver.GetAllDriverResponse
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartPayload
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartResponse
import com.feylabs.halalkan.data.remote.reqres.order.DetailOrderResponse
import com.feylabs.halalkan.data.remote.reqres.order.OrderStatusResponse
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryResponse
import com.feylabs.halalkan.data.remote.reqres.order.resto.OrderByRestoPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.product.SearchProductResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import com.feylabs.halalkan.data.remote.reqres.resto.operating_hour.RestoOperatingHourResponse
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateRestoColumnResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface RestoService {


    @GET("/users")
    suspend fun getUsers(): Response<UserResponse>

    @GET("fe/restoran/cert")
    suspend fun getCert(): Response<RestaurantCertificationResponse>

    @GET("fe/restoran/food-type")
    suspend fun getFoodType(): Response<FoodTypeResponse>


    @GET("fe/restoran/search")
    suspend fun searchResto(
        @Query("name") name: String? = null,
        @Query("certification_id") certificationId: Int? = null,
        @Query("type_food_id") typeFoodId: Int?,
        @Query("perPage") perPage: Int? = 10,
        @Query("sortBy") sortBy: String? = null,
        @Query("page") page: Int? = 1
    ): Response<SearchRestoResponse>


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

    @FormUrlEncoded
    @POST("restoran/editAddress/{restoId}")
    suspend fun updateRestoAddress(
        @Field("lat") latitude: Double,
        @Field("long") longitude: Double,
        @Field("address") address: String,
        @Path("restoId") restoId: String,
        @Field("kecamatan") kecamatan: String,
        @Field("kelurahan") kelurahan: String,
    ): Response<GeneralApiResponse>


    @FormUrlEncoded
    @POST("restoran/operatingHour/create/{restoId}")
    suspend fun createOperatingHour(
        @Field("hour_start") hourStart: String,
        @Field("hour_end") hourEnd: String,
        @Field("day_code") dayCode: Int,
        @Field("day") day: String = "-",
        @Field("hour") hour: String = "-",
        @Path("restoId") restoId: String,
    ): Response<GeneralApiResponse>

    @FormUrlEncoded
    @POST("restoran/operatingHour/edit/{restoId}/{hourId}")
    suspend fun updateOperatingHour(
        @Field("hour_start") hourStart: String,
        @Field("hour_end") hourEnd: String,
        @Field("day_code") dayCode: Int,
        @Field("day") day: String = "-",
        @Field("hour") hour: String = "-",
        @Path("restoId") restoId: String,
        @Path("hourId") hourId: String,
    ): Response<GeneralApiResponse>

    @DELETE("/api/v1/restoran/operatingHour/delete/{restoId}/{hourId}")
    suspend fun deleteOperatingHour(
        @Path("restoId") restoId: String,
        @Path("hourId") hourId: String,
    ): Response<GeneralApiResponse>

    @GET("restoran/operatingHour/getByResto/{restoId}")
    suspend fun getRestoOperatingHour(
        @Path("restoId") restoId: String,
    ): Response<RestoOperatingHourResponse>

    @GET("fe/foods/{foodId}/update-availability")
    suspend fun updateFoodAvailability(
        @Path("foodId") foodId: String,
        @Query("is_visible") isAvailable: Int,
    ): Response<GeneralApiResponse>


    @POST("foods/category/createCategory/{id}")
    suspend fun createRestoFoodCategory(
        @Body file: RequestBody?,
        @Path("id") restoId: String,
    ): Response<ResponseBody?>?

    @PUT("foods/category/editCategory/{category_id}")
    suspend fun editRestoFoodCategory(
        @Path("category_id") categoryId: String,
        @Query("resto_id") restoId: String,
        @Query("name") categoryName: String,
    ): Response<GeneralApiResponse>

    @DELETE("foods/category/deleteCategory/{category_id}")
    suspend fun deleteRestoFoodCategory(
        @Path("category_id") categoryId: String,
    ): Response<GeneralApiResponse>

    @Multipart
    @POST("foods/store")
    suspend fun createFood(
        @Part("image\"; filename=\"image") file: RequestBody?,
        @Part("type_food_id") typeFoodId: Int?,
        @Part("category_id") categoryId: Int?,
        @Part("restoran_id") restoran_id: Int?,
        @Part("description") description: String?,
        @Part("name") name: String?,
        @Part("price") price: Int?,
        @Part("is_visible") is_visible: Int = 1,
    ): Response<GeneralApiResponse?>?

    @Multipart
    @POST("foods/editFood/{foodId}")
    suspend fun editFood(
        @Path("foodId") foodId: String,
        @Part("image\"; filename=\"image") file: RequestBody?,
        @Part("type_food_id") typeFoodId: Int?,
        @Part("category_id") categoryId: Int?,
        @Part("restoran_id") restoran_id: Int?,
        @Part("description") description: String?,
        @Part("name") name: String?,
        @Part("price") price: Int?,
    ): Response<GeneralApiResponse?>?

    @GET("foods/{id}/detail")
    suspend fun getFoodDetail(
        @Path("id") foodId: String,
    ): Response<FoodModelResponse>

    @DELETE("foods/deleteFood/{id}")
    suspend fun deleteFood(
        @Path("id") foodId: String,
    ): Response<GeneralApiResponse>

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

    @PUT("orders/carts/approvedOrder/{orderId}")
    suspend fun orderApprove(
        @Path("orderId") orderId: String,
    ): Response<GeneralApiResponse>

    @PUT("orders/carts/approvedDeliv/{orderId}")
    suspend fun orderDelivered(
        @Path("orderId") orderId: String,
        @Query("driver_id") driverId: Int
    ): Response<GeneralApiResponse>


    @POST("orders/carts/finishOrder/{orderId}")
    suspend fun orderFinish(
        @Body body: RequestBody?,
        @Path("orderId") orderId: String,
    ): Response<DetailOrderResponse?>?

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

    @DELETE("driver/deleteDriver/{driverId}")
    suspend fun deleteDriver(
        @Path("driverId") driverId: String,
    ): Response<GeneralApiResponse>


    @Multipart
    @POST("driver/register")
    suspend fun addNewDriver(
        @Part("img\"; filename=\"img") file: RequestBody?,
        @Part("name") name: String,
        @Part("email") email: String,
        @Part("phone_number") phone_number: String,
        @Part("password") password: String,
        @Part("confirm_password") confirm_password: String,
    ): Response<RegisterResponse?>?


}
package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.driver.MyProfileDriverResponse
import com.feylabs.halalkan.data.remote.reqres.driver.UpdateLocationPayload
import com.feylabs.halalkan.data.remote.reqres.order.*
import com.feylabs.halalkan.data.remote.reqres.resto.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface DriverService {


    //get food per resto category
    @GET("fe/driver/myOrders")
    suspend fun getOrderByDriver(
        @Query("perPage") perPage: Int = 5,
        @Query("page") page: Int = 1,
    ): Response<DriverOrderPaginationResponse>

    //get food per resto category
    @GET("driver/driverProfile")
    suspend fun getDriverProfile(): Response<MyProfileDriverResponse>

    @PUT("driver/updateLocation/:driverId")
    suspend fun updateLocation(
        @Body body: UpdateLocationPayload?,
        @Path("driver_id") driverId: Int
    ): Response<MyProfileDriverResponse>

}
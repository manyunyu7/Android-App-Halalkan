package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.data.remote.reqres.product.ProductCateogryResponse
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.FoodTypeResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestaurantCertificationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.data.remote.reqres.resto.food.RestoFoodByCommonCategoryResponse
import retrofit2.Response
import retrofit2.http.*


interface ProductService {


    @GET("fe/products/category")
    suspend fun getProductCategory(): Response<ProductCateogryResponse>


}
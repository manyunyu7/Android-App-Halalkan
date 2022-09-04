package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.forum.*
import com.feylabs.halalkan.data.remote.reqres.product.ProductCategoryResponse
import com.feylabs.halalkan.data.remote.reqres.product.ProductDetailResponse
import com.feylabs.halalkan.data.remote.reqres.product.ProductListPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.product.SearchProductResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ProductService {

    @GET("fe/products/category")
    suspend fun getAllCategory(
    ): Response<ProductCategoryResponse>

    @GET("fe/products/category/{id}")
    suspend fun getProductOnCategory(
        @Path("id") categoryId: String,
        @Query("perPage") perPage: Int = 5,
        @Query("page") page: Int = 1
    ): Response<ProductListPaginationResponse>

    @GET("fe/products/search")
    suspend fun searchProduct(
        @Query("category") categoryId: Int? = null,
        @Query("name") name: String = ""
    ): Response<SearchProductResponse>

    @GET("products/detail/{productId}")
    suspend fun getProductDetail(
        @Path("productId") productId: String
    ): Response<ProductDetailResponse>

}
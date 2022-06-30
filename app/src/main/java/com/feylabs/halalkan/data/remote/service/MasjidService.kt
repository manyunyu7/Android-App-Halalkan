package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.*
import com.feylabs.halalkan.data.remote.reqres.masjid.*
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface MasjidService {

    @GET("masjids/showAll")
    suspend fun showAllMasjid(
    ): Response<MasjidResponseWithoutPagination>

    @GET("masjids/showAll")
    suspend fun showAllMasjidPaginate(
        @Query("isPaginate") isPaginate:Boolean=true,
        @Query("page") page:Int=1
    ): Response<AllMasjidPaginationResponse>

    @GET("masjids/{id}/photos")
    suspend fun getMasjidPhotos(
        @Path("id") masjidId: String,
    ): Response<MasjidPhotosResponse>

    @GET("masjids/{id}")
    suspend fun getMasjidDetail(
        @Path("id") masjidId: String,
    ): Response<MasjidDetailResponse>

    @GET("masjids/{id}/reviews/")
    suspend fun getMasjidReviews(
        @Path("id") masjidId: String,
        @Query("perPage") perPage:Int=5,
        @Query("page") page:Int=1
    ): Response<MasjidReviewPaginationResponse>

}
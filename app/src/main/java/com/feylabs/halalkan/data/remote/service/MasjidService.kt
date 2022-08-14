package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidDetailResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface MasjidService {

    @GET("masjids/showAll")
    suspend fun showAllMasjid(
    ): Response<MasjidResponseWithoutPagination>

    @GET("masjids/showAll")
    suspend fun showAllMasjidPaginate(
        @Query("isPaginate") isPaginate: Boolean = true,
        @Query("page") page: Int = 1
    ): Response<AllMasjidPaginationResponse>

    @GET("fe/masjids/{id}/photos")
    suspend fun getMasjidPhotos(
        @Path("id") masjidId: String,
    ): Response<MasjidPhotosResponse>

    @GET("masjids/{id}")
    suspend fun getMasjidDetail(
        @Path("id") masjidId: String,
    ): Response<MasjidDetailResponse>

    @GET("fe/masjids/{id}/reviews/")
    suspend fun getMasjidReviews(
        @Path("id") masjidId: String,
        @Query("perPage") perPage: Int = 5,
        @Query("page") page: Int = 1
    ): Response<MasjidReviewPaginationResponse>

    @POST("reviewMasjid/store/{masjidId}")
    suspend fun createReviewMasjid(
        @Body file: RequestBody?,
        @Path("masjidId") masjidId: String,
        ): Response<ResponseBody?>?

}
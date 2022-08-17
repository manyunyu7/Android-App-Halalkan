package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.data.remote.reqres.forum.AllForumPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.forum.CreateForumResponse
import com.feylabs.halalkan.data.remote.reqres.forum.ForumCategoryResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateRestoColumnResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface ForumService {

    @GET("fe/forums/category")
    suspend fun getForumsCategory(
    ): Response<ForumCategoryResponse>

    @GET("fe/forums/all-paginate")
    suspend fun getAllForum(
        @Query("perPage") perPage: Int = 5,
        @Query("page") page: Int = 1
    ): Response<AllForumPaginationResponse>

    @POST("forums/store")
    suspend fun createForum(
        @Body file: RequestBody?,
        ): Response<CreateForumResponse?>?


}
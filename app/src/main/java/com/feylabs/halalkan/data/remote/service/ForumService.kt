package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.forum.*
import okhttp3.RequestBody
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

    @POST("forums/update/{forumId}")
    suspend fun updateForum(
        @Path("forumId") forumId: Int,
        @Body file: RequestBody?,
        @Query("is_deleting_image") isDeletingImage: Boolean = false
    ): Response<CreateForumResponse?>?

    @POST("forums/like/{forumId}")
    suspend fun likeForum(
        @Path("forumId") forumId: Int,
    ): Response<GeneralApiResponse>

    @POST("forums/unlike/{forumId}")
    suspend fun unlikeForum(
        @Path("forumId") forumId: Int,
    ): Response<GeneralApiResponse>

    @POST("comments/like/{Id}")
    suspend fun likeCommentForum(
        @Path("Id") commentId: Int,
    ): Response<GeneralApiResponse>

    @POST("comments/unlike/{Id}")
    suspend fun unlikeComment(
        @Path("Id") commentId: Int,
    ): Response<GeneralApiResponse>

   @GET("forums/detailForum/{forumId}")
    suspend fun detailForum(
        @Path("forumId") forumId: Int,
    ): Response<ForumDetailResponse>

   @GET("forums/comment/{forumId}")
    suspend fun commentOnForum(
        @Path("forumId") forumId: Int,
    ): Response<ForumCommentResponse>

    @POST("comments/store")
    suspend fun createComment(
        @Body body: CreateCommentPayload
    ): Response<AddCommentResponse>

}
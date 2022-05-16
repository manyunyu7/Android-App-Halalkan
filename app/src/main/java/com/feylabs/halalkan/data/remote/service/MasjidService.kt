package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.*
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface MasjidService {


    @GET("/masjids/showAll")
    suspend fun getMasjids(): Response<MasjidResponseWithoutPagination>

    @GET("/posts")
    suspend fun getPosts(): Response<PostResponse>

    @GET("posts/{postId}/")
    fun getPostDetail(
        @Path("postId") postId: String,
    ): Response<PostDetailResponse>

    @GET("users/{userId}/")
    suspend fun getUserDetail(
        @Path("userId") userId: String,
    ): Response<UserDetailResponse>

    @GET("users/{userId}/albums")
    suspend fun getUserAlbum(
        @Path("userId") userId: String,
    ): Response<UserAlbumResponse>

    @GET("albums/{albumId}/photos")
    suspend fun getPhotoByAlbum(
        @Path("albumId") albumId: String,
    ): Response<AlbumPhotoResponse>

    @GET("posts/{postId}/comments")
    suspend fun getPostComments(
        @Path("postId") postId: String,
    ): Response<PostCommentResponse>

}
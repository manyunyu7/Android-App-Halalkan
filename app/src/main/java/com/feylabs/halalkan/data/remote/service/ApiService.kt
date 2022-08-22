package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.*
import com.feylabs.halalkan.data.remote.reqres.auth.*
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    /*
    Note Belajar :
    - Suspend fun harus return datanya langsung
    - Call return enqueue, untuk callback
    - Resource untuk digunakan bareng RxJava, Coroutines (suspend)
     */

    @GET("/users")
    suspend fun getUsers(): Response<UserResponse>

    @GET("/posts")
    suspend fun getPosts(): Response<PostResponse>

    /**
     * Login Request
     */
    @POST("users/login")
    suspend fun login(
        @Body body: LoginBodyRequest
    ): Response<LoginResponse>

    @POST("users/register")
    suspend fun register(
        @Body body: RegisterBodyRequest
    ): Response<RegisterResponse>

    @GET("posts/{postId}/")
    fun getPostDetail(
        @Path("postId") postId: String,
    ): Response<PostDetailResponse>

    @GET("users/me/profile")
    suspend fun userProfile(
    ): Response<MyProfileResponse>

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

    @GET("masjids/showAll")
    suspend fun getMasjids(
        @Query("isPaginate") isPaginate: Boolean = false
    ): Response<MasjidResponseWithoutPagination>


}
package com.feylabs.halalkan.data.remote.service

import com.feylabs.halalkan.data.remote.reqres.*
import com.feylabs.halalkan.data.remote.reqres.auth.*
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import okhttp3.RequestBody
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

    @GET("fe/users/{id}/profile")
    suspend fun getUserProfile(
        @Path("id") userId: String
    ): Response<UserModel>

    @FormUrlEncoded
    @POST("fe/users/{id}/reset-password")
    suspend fun resetPassword(
        @Path("id") userId: String,
        @Field("new_password") newPassword:String
    ): Response<GeneralApiResponse>


    @Multipart
    @POST("driver/editDriver/{driverId}")
    suspend fun editDriver(
        @Path("driverId") driverId: String,
        @Part("img\"; filename=\"img") file: RequestBody?,
        @Part("name") name: String,
        @Part("email") email: String,
        @Part("phone_number") phone_number: String,
    ): Response<GeneralApiResponse?>?

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
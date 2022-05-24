package com.feylabs.halalkan.data.repository

import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest

class QumparanRepository(
    private val remoteDs: RemoteDataSource,
) {

    suspend fun getUsers() = remoteDs.getUsers()
    suspend fun getPosts() = remoteDs.getPosts()
    suspend fun getPostDetail(postId: String) = remoteDs.getPostDetail(postId)
    suspend fun getPostComment(postId: String) = remoteDs.getPostComment(postId)
    suspend fun getUserDetail(userId: String) = remoteDs.getUserDetail(userId)
    suspend fun getUserAlbum(userId: String) = remoteDs.getUserAlbum(userId)
    suspend fun getAlbumPhoto(albumId: String) = remoteDs.getAlbumPhoto(albumId)

    suspend fun login(bodyRequest: LoginBodyRequest) = remoteDs.login(bodyRequest)
    suspend fun register(bodyRequest: RegisterBodyRequest) = remoteDs.register(bodyRequest)

    suspend fun getMasjids() = remoteDs.getMasjids()
}
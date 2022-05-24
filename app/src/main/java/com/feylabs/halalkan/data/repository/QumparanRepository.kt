package com.feylabs.halalkan.data.repository

import com.feylabs.halalkan.data.remote.RemoteDataSource

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

    suspend fun getMasjids() = remoteDs.getMasjids()
}
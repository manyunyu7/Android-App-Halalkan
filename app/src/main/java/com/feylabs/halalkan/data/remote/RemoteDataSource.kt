package com.feylabs.halalkan.data.remote

import com.feylabs.halalkan.data.remote.service.ApiService
import com.feylabs.halalkan.data.remote.service.MasjidService
import com.feylabs.halalkan.data.remote.service.TranslatorService

class RemoteDataSource(
    private val commonService: ApiService,
    private val masjidService: MasjidService,
    private val translationService: TranslatorService,
) {

    /**
     * get news
     * @param body,callback
     */
    suspend fun getUsers() = commonService.getUsers()


    suspend fun getPosts() = commonService.getPosts()

    suspend fun getPostDetail(postId: String) = commonService.getPostDetail(postId)
    suspend fun getPostComment(postId: String) = commonService.getPostComments(postId)

    suspend fun getUserDetail(userId: String) = commonService.getUserDetail(userId)
    suspend fun getUserAlbum(userId: String) = commonService.getUserAlbum(userId)
    suspend fun getAlbumPhoto(albumId: String) = commonService.getPhotoByAlbum(albumId)

    suspend fun getMasjids() = commonService.getMasjids()

    suspend fun getTranslation(
        langSource: String, target: String, text: String
    ) = translationService.translate(langSource = langSource, target = target, text = text)

    suspend fun getTextToSpeech(
        langSource: String, text: String
    ) = translationService.getTTS(lang = langSource, text = text)

    suspend fun getAllMasjid() = masjidService.showAllMasjid()

    suspend fun getMasjidPhotos(id: String) = masjidService.getMasjidPhotos(id)
    suspend fun getMasjidDetail(id: String) = masjidService.getMasjidDetail(id)

}
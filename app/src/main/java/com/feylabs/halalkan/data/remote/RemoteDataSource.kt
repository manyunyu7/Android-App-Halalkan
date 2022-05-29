package com.feylabs.halalkan.data.remote

import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidDetailResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewsResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.service.ApiService
import com.feylabs.halalkan.data.remote.service.MasjidService
import com.feylabs.halalkan.data.remote.service.PrayerTimeAladhanService
import com.feylabs.halalkan.data.remote.service.TranslatorService
import com.feylabs.halalkan.utils.Network
import retrofit2.Response

class RemoteDataSource(
    private val commonService: ApiService,
    private val masjidService: MasjidService,
    private val translationService: TranslatorService,
    private val prayerTimeService: PrayerTimeAladhanService,
) : RemoteDataSourceInterface {

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

    override suspend fun getMasjids() = commonService.getMasjids()

    override suspend fun getMasjidReviews(masjidId:String): Response<MasjidReviewsResponse> {
        return masjidService.getMasjidReviews(masjidId)
    }

    override suspend fun login(loginBodyRequest: LoginBodyRequest) = commonService.login(loginBodyRequest)
    override suspend fun register(bodyRequest: RegisterBodyRequest) = commonService.register(bodyRequest)

    override suspend fun getTranslation(
        langSource: String, target: String, text: String
    ) = translationService.translate(langSource = langSource, target = target, text = text)

    override suspend fun getTextToSpeech(
        langSource: String, text: String
    ) = translationService.getTTS(lang = langSource, text = text)

    override suspend fun getAllMasjid() = masjidService.showAllMasjid()

    override suspend fun getMasjidPhotos(id: String): Response<MasjidPhotosResponse> =
        masjidService.getMasjidPhotos(id)


    override suspend fun getPrayerTimeSingleDate(
        time: String,
        lat: String,
        long: String,
        method: String
    ): Response<PrayerTimeAladhanSingleDateResponse> {
        val url = Network.BASE_URL_ALADHAN_V1 + time + ""
        return prayerTimeService.getSinglePrayerTime(
            fullUrl = url,
            latitude = lat,
            longitude = long,
            method = method
        )
    }

    override suspend fun getMasjidDetail(id: String): Response<MasjidDetailResponse> {
        return masjidService.getMasjidDetail(id)
    }


}
package com.feylabs.halalkan.data.remote

import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavMasjidResponse
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavRestoResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidDetailResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.service.*
import com.feylabs.halalkan.utils.Network
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class RemoteDataSource(
    private val commonService: ApiService,
    private val masjidService: MasjidService,
    private val translationService: TranslatorService,
    private val prayerTimeService: PrayerTimeAladhanService,
    private val restoService: RestoService,
    private val favoriteService: FavoriteService,
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
    override suspend fun getMasjidsPagination(page: Int): Response<AllMasjidPaginationResponse> =
        masjidService.showAllMasjidPaginate(page = page)

    override suspend fun getMasjidReviews(
        masjidId: String,
        perPage: Int,
        page: Int,
    ): Response<MasjidReviewPaginationResponse> {
        return masjidService.getMasjidReviews(masjidId, page = page, perPage = perPage)
    }

    override suspend fun createMasjidReview(
        masjidId: String,
        body: RequestBody
    ): Response<ResponseBody?>? {
        return masjidService.createReviewMasjid(body, masjidId)
    }


    override suspend fun login(loginBodyRequest: LoginBodyRequest) =
        commonService.login(loginBodyRequest)

    override suspend fun register(bodyRequest: RegisterBodyRequest) =
        commonService.register(bodyRequest)

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

    override suspend fun getRestoCert(): Response<RestaurantCertificationResponse> {
        return restoService.getCert()
    }

    override suspend fun getRestoAll(): Response<AllRestoNoPagination> {
        return restoService.getAllRaw()
    }

    override suspend fun getFoodType(): Response<FoodTypeResponse> {
        return restoService.getFoodType()
    }

    override suspend fun getRestoDetail(id: String): Response<RestoDetailResponse> {
        return restoService.getDetail(id)
    }

    override suspend fun getRestoFoodByCommonCategory(
        restoId: String,
        categoryId: String
    ) = restoService.getFoodByResto(categoryId)

    override suspend fun createRestoReview(
        restoId: String,
        body: RequestBody
    ): Response<ResponseBody?>? = restoService.createReview(body, restoId)

    override suspend fun getRestoReviews(
        restoId: String,
        perPage: Int,
        page: Int
    ): Response<RestoReviewPaginationResponse> =
        restoService.getReviews(restoId, page = page, perPage = perPage)


    override suspend fun getFoodCategoryOnResto(id: String): Response<FoodCategoryResponse> {
        return restoService.getFoodCategoryOnResto(id)
    }

    override suspend fun getFoodByCategory(id: String): Response<AllFoodByRestoResponse> {
        return restoService.getFoodByCategory(id)
    }

    override suspend fun getAllFoodByResto(id: String): Response<AllFoodByRestoResponse> {
        return restoService.getAllFoodOnResto(id)
    }

    override suspend fun addFavoriteMasjid(masjidId: String): Response<AddFavMasjidResponse> {
        return favoriteService.addFavoriteMasjid(masjidId)
    }

    override suspend fun addFavoriteResto(restoId: String): Response<AddFavRestoResponse> {
        return favoriteService.addFavoriteResto(restoId)
    }

}
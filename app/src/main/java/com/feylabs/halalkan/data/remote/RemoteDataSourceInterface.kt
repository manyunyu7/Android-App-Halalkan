package com.feylabs.halalkan.data.remote

import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.LoginResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import com.feylabs.halalkan.data.remote.reqres.driver.GetAllDriverResponse
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavMasjidResponse
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavRestoResponse
import com.feylabs.halalkan.data.remote.reqres.forum.*
import com.feylabs.halalkan.data.remote.reqres.masjid.*
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.order.*
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryResponse
import com.feylabs.halalkan.data.remote.reqres.order.resto.OrderByRestoPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateRestoColumnResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TiktokTextToSpeechResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TranslateResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response


interface RemoteDataSourceInterface {

    suspend fun getPrayerTimeSingleDate(
        time: String,
        lat: String,
        long: String,
        method: String
    ): Response<PrayerTimeAladhanSingleDateResponse>

    suspend fun getMasjidDetail(id: String): Response<MasjidDetailResponse>
    suspend fun getMasjidPhotos(id: String): Response<MasjidPhotosResponse>
    suspend fun getAllMasjid(): Response<MasjidResponseWithoutPagination>

    suspend fun getTextToSpeech(
        langSource: String, text: String
    ): Response<TiktokTextToSpeechResponse>

    suspend fun getTranslation(
        langSource: String, target: String, text: String
    ): Response<TranslateResponse>

    suspend fun register(bodyRequest: RegisterBodyRequest): Response<RegisterResponse>

    suspend fun login(loginBodyRequest: LoginBodyRequest): Response<LoginResponse>

    suspend fun getMasjids(): Response<MasjidResponseWithoutPagination>
    suspend fun getMasjidsPagination(page:Int): Response<AllMasjidPaginationResponse>
    suspend fun getMasjidReviews(
        masjidId: String,
        perPage: Int,
        page: Int
    ): Response<MasjidReviewPaginationResponse>

    suspend fun createMasjidReview(masjidId: String,body:RequestBody) : Response<ResponseBody?>?



    suspend fun getRestoCert() : Response<RestaurantCertificationResponse>
    suspend fun getRestoAll() : Response<AllRestoNoPagination>
    suspend fun getFoodType() : Response<FoodTypeResponse>
    suspend fun getRestoDetail(id:String) : Response<RestoDetailResponse>
    suspend fun getRestoFoodByCommonCategory(restoId:String,categoryId:String) : Response<AllFoodByRestoResponse>
    suspend fun createRestoReview(restoId: String,body:RequestBody) : Response<ResponseBody?>?
    suspend fun getRestoReviews(
        restoId: String,
        perPage: Int,
        page: Int
    ): Response<RestoReviewPaginationResponse>

    suspend fun createResto(body:RequestBody) : Response<ResponseBody?>?
    suspend fun createRestoFoodCategory(id:String,body:RequestBody) : Response<ResponseBody?>?
    suspend fun updateRestoColumn(
        id: String,
        pathupdate: String,
        body: RequestBody
    ): Response<UpdateRestoColumnResponse?>?



    suspend fun getFoodCategoryOnResto(id:String) : Response<FoodCategoryResponse>
    suspend fun getFoodByCategory(id:String) : Response<AllFoodByRestoResponse>
    suspend fun getAllFoodByResto(id:String) : Response<AllFoodByRestoResponse>

    suspend fun getMyResto() : Response<AllRestoNoPagination>

    suspend fun addFavoriteMasjid(masjidId:String) : Response<AddFavMasjidResponse>
    suspend fun addFavoriteResto(restoId:String) : Response<AddFavRestoResponse>


    suspend fun createForum(body:RequestBody) : Response<CreateForumResponse?>?
    suspend fun updateForum(forumId:String,body:RequestBody,isDeletingImage:Boolean) : Response<CreateForumResponse?>?
    suspend fun getForumCategory() : Response<ForumCategoryResponse>
    suspend fun getAllForumPaginate(page:Int,perPage:Int) : Response<AllForumPaginationResponse>
    suspend fun likeForum(forumId:Int) : Response<GeneralApiResponse>
    suspend fun unlikeForum(forumId:Int) : Response<GeneralApiResponse>
    suspend fun getDetailForum(forumId: Int): Response<ForumDetailResponse>
    suspend fun getCommentForum(forumId: Int): Response<ForumCommentResponse>
    suspend fun likeComment(forumId: Int): Response<GeneralApiResponse>
    suspend fun unlikeComment(forumId: Int): Response<GeneralApiResponse>
    suspend fun createComment(body: CreateCommentPayload): Response<AddCommentResponse>
    suspend fun deleteForum(forumId: Int): Response<GeneralApiResponse>
    suspend fun deleteComment(commentId: Int): Response<GeneralApiResponse>
    suspend fun checkout(body: CreateCartPayload): Response<CreateCartResponse>
    suspend fun getHistoryOrder(): Response<OrderHistoryResponse>
    suspend fun getRestoHistoryOrder(
        restoId: String,
        page: Int,
        perPage: Int,
        mStatus: String?): Response<OrderByRestoPaginationResponse>

    suspend fun orderReject(orderId: Int, reason: String): Response<GeneralApiResponse>
    suspend fun orderApprove(orderId: Int): Response<GeneralApiResponse>
    suspend fun orderDetail(orderId: Int): Response<DetailOrderResponse>

    suspend fun getOrderStatus(): Response<OrderStatusResponse>
    suspend fun geDriverOnResto(restoId: String): Response<GetAllDriverResponse>
    suspend fun addNewDriverByResto(bodyRequest: RegisterBodyRequest): Response<RegisterResponse>
    suspend fun orderDelivered(orderId: Int, driverId: Int): Response<GeneralApiResponse>
    suspend fun getDriverOrder(page: Int, perPage: Int): Response<DriverOrderPaginationResponse>
    suspend fun orderFinished(orderId: String, body: RequestBody): Response<DetailOrderResponse?>?
    suspend fun createRestoFood(
        typeFoodId: Int?,
        categoryId: Int?,
        restoran_id: Int?,
        description: String?,
        name: String?,
        price: Int?,
        image: RequestBody
    ): Response<GeneralApiResponse?>?

    suspend fun deleteFood(foodId: String): Response<GeneralApiResponse>
    suspend fun getFoodDetail(foodId: String): Response<FoodModelResponse>
    suspend fun editRestoFood(
        foodId: String,
        typeFoodId: Int?,
        categoryId: Int?,
        restoran_id: Int?,
        description: String?,
        name: String?,
        price: Int?,
        image: RequestBody?
    ): Response<GeneralApiResponse?>?
}
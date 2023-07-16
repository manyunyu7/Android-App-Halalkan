package com.feylabs.halalkan.data.remote

import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.LoginResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.driver.GetAllDriverResponse
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavMasjidResponse
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavRestoResponse
import com.feylabs.halalkan.data.remote.reqres.forum.*
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidDetailResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartPayload
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartResponse
import com.feylabs.halalkan.data.remote.reqres.order.DetailOrderResponse
import com.feylabs.halalkan.data.remote.reqres.order.DriverOrderPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryResponse
import com.feylabs.halalkan.data.remote.reqres.order.resto.OrderByRestoPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.reqres.product.ProductCategoryResponse
import com.feylabs.halalkan.data.remote.reqres.product.ProductListPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateRestoColumnResponse
import com.feylabs.halalkan.data.remote.service.*
import com.feylabs.halalkan.utils.Network
import com.mapbox.maps.extension.style.expressions.dsl.generated.product
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Part

class RemoteDataSource(
    private val commonService: ApiService,
    private val masjidService: MasjidService,
    private val translationService: TranslatorService,
    private val prayerTimeService: PrayerTimeAladhanService,
    private val restoService: RestoService,
    private val favoriteService: FavoriteService,
    private val forumService: ForumService,
    private val driverService: DriverService,
    private val productService: ProductService,
) : RemoteDataSourceInterface {

    /**
     * get news
     * @param body,callback
     */
    suspend fun getUsers() = commonService.getUsers()
    suspend fun getMyProfile() = commonService.userProfile()
    suspend fun getUserProfile(id: String) = commonService.getUserProfile(id)
    suspend fun resetUserPassword(id: String, newPassword: String) = commonService.resetPassword(
        userId = id, newPassword = newPassword
    )

    suspend fun changePassword(oldPassword: String, newPassword: String) =
        commonService.updatePassword(
            oldPassword = oldPassword, newPassword = newPassword, confirmPassword = newPassword
        )

    suspend fun updateProfile(
        body: RequestBody
    ) = commonService.editProfile(body)


    suspend fun getPosts() = commonService.getPosts()

    suspend fun getPostDetail(postId: String) = commonService.getPostDetail(postId)
    suspend fun getPostComment(postId: String) = commonService.getPostComments(postId)

    suspend fun getUserDetail(userId: String) = commonService.getUserDetail(userId)
    suspend fun getUserAlbum(userId: String) = commonService.getUserAlbum(userId)
    suspend fun getAlbumPhoto(albumId: String) = commonService.getPhotoByAlbum(albumId)

     suspend fun getMasjids() = commonService.getMasjids()
     suspend fun getMasjidsPagination(page: Int): Response<AllMasjidPaginationResponse> =
        masjidService.showAllMasjidPaginate(page = page)

     suspend fun getMasjidReviews(
        masjidId: String,
        perPage: Int,
        page: Int,
    ): Response<MasjidReviewPaginationResponse> {
        return masjidService.getMasjidReviews(masjidId, page = page, perPage = perPage)
    }

     suspend fun createMasjidReview(
        masjidId: String,
        body: RequestBody
    ): Response<ResponseBody?>? {
        return masjidService.createReviewMasjid(body, masjidId)
    }


     suspend fun login(loginBodyRequest: LoginBodyRequest) =
        commonService.login(loginBodyRequest)

     suspend fun register(bodyRequest: RegisterBodyRequest) =
        commonService.register(bodyRequest)

     suspend fun getTranslation(
        langSource: String, target: String, text: String
    ) = translationService.translate(langSource = langSource, target = target, text = text)

     suspend fun getTextToSpeech(
        langSource: String, text: String
    ) = translationService.getTTS(lang = langSource, text = text)

     suspend fun getAllMasjid() = masjidService.showAllMasjid()

     suspend fun getMasjidPhotos(id: String): Response<MasjidPhotosResponse> =
        masjidService.getMasjidPhotos(id)


     suspend fun getPrayerTimeSingleDate(
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

    suspend fun getMasjidType() = masjidService.getMasjidType()

     suspend fun getMasjidDetail(id: String): Response<MasjidDetailResponse> {
        return masjidService.getMasjidDetail(id)
    }

    //product
     suspend fun getAllProductCategory(): Response<ProductCategoryResponse> =
        productService.getAllCategory()

    suspend fun getProductDetail(
        productId: String,
    ) = productService.getProductDetail(productId)

    suspend fun searchProduct(
        categoryId: Int?,
        productName: String?,
        code: String?,
    ) = productService.searchProduct(categoryId = categoryId, productName, code)

    suspend fun getProductOnCategory(
        categoryId: String,
        perPage: Int,
        page: Int
    ): Response<ProductListPaginationResponse> =
        productService.getProductOnCategory(categoryId, perPage = perPage, page = page)

     suspend fun getRestoCert(): Response<RestaurantCertificationResponse> {
        return restoService.getCert()
    }

    suspend fun searchResto(
        name: String? = null,
        certificationId: Int?,
        typeFoodId: Int?,
        perPage: Int? = null,
        page: Int? = 1,
        sortBy: String? = null
    ) = restoService.searchResto(
        name = name, sortBy = sortBy,
        certificationId = certificationId, typeFoodId = typeFoodId, perPage = perPage, page = page
    )

    suspend fun searchMasjid(
        name: String? = null,
        typeId: Int?,
        perPage: Int? = null,
        page: Int? = 1,
        sortBy: String? = null
    ) = masjidService.searchMasjid(
        name = name,
        typeId = typeId,
        perPage = perPage,
        page = page
    )


     suspend fun getRestoAll(): Response<AllRestoNoPagination> {
        return restoService.getAllRaw()
    }

     suspend fun getFoodType(): Response<FoodTypeResponse> {
        return restoService.getFoodType()
    }

     suspend fun getRestoDetail(id: String): Response<RestoDetailResponse> {
        return restoService.getDetail(id)
    }

     suspend fun getRestoFoodByCommonCategory(
        restoId: String,
        categoryId: String
    ) = restoService.getFoodByResto(categoryId)

     suspend fun createRestoReview(
        restoId: String,
        body: RequestBody
    ): Response<ResponseBody?>? = restoService.createReview(body, restoId)

     suspend fun getRestoReviews(
        restoId: String,
        perPage: Int,
        page: Int
    ): Response<RestoReviewPaginationResponse> =
        restoService.getReviews(restoId, page = page, perPage = perPage)

     suspend fun createResto(body: RequestBody): Response<ResponseBody?>? =
        restoService.createResto(body)

     suspend fun createRestoFoodCategory(
        id: String,
        body: RequestBody
    ): Response<ResponseBody?>? {
        return restoService.createRestoFoodCategory(body, id)
    }

     suspend fun editRestoFoodCategory(
        id: String,
        restoid: String,
        category_name: String
    ) = restoService.editRestoFoodCategory(
        restoId = restoid,
        categoryName = category_name,
        categoryId = id
    )

     suspend fun deleteRestoFoodCategory(
        id: String,
    ) = restoService.deleteRestoFoodCategory(
        categoryId = id
    )

     suspend fun updateFoodAvailability(
        id: String,
        isAvailable: Int
    ) = restoService.updateFoodAvailability(
        id, isAvailable
    )


     suspend fun deleteFood(foodId: String): Response<GeneralApiResponse> =
        restoService.deleteFood(foodId)

     suspend fun getFoodDetail(foodId: String): Response<FoodModelResponse> =
        restoService.getFoodDetail(foodId)

     suspend fun editRestoFood(
        foodId: String,
        typeFoodId: Int?,
        categoryId: Int?,
        restoran_id: Int?,
        description: String?,
        name: String?,
        price: Int?,
        image: RequestBody?
    ): Response<GeneralApiResponse?>? {
        return restoService.editFood(
            foodId,
            file = image,
            typeFoodId, categoryId, restoran_id, description, name, price
        )
    }

     suspend fun createRestoFood(
        typeFoodId: Int?,
        categoryId: Int?,
        restoran_id: Int?,
        description: String?,
        name: String?,
        price: Int?,
        image: RequestBody
    ): Response<GeneralApiResponse?>? {
        return restoService.createFood(
            file = image,
            typeFoodId, categoryId, restoran_id, description, name, price
        )
    }

     suspend fun createRestoOperatingHour(
        restoId: String,
        dayCode: Int,
        startHour: String,
        endHour: String,
    ) = restoService.createOperatingHour(
        hourStart = startHour,
        hourEnd = endHour,
        dayCode = dayCode,
        restoId = restoId
    )

     suspend fun updateRestoOperatingHour(
        hourId: String,
        restoId: String,
        dayCode: Int,
        startHour: String,
        endHour: String,
    ) = restoService.updateOperatingHour(
        hourId = hourId,
        hourStart = startHour,
        hourEnd = endHour,
        dayCode = dayCode,
        restoId = restoId
    )

     suspend fun deleteRestoOperatingHour(
        hourId: String,
        restoId: String,
    ) = restoService.deleteOperatingHour(
        hourId = hourId,
        restoId = restoId
    )

     suspend fun getRestoOperatingHour(
        restoId: String,
    ) = restoService.getRestoOperatingHour(restoId)

     suspend fun updateRestoColumn(
        id: String,
        pathupdate: String,
        body: RequestBody
    ): Response<UpdateRestoColumnResponse?>? {
        return restoService.updateRestoColumn(restoId = id, urlupdate = pathupdate, file = body)
    }

    suspend fun updateRestoAddress(
        id: String,
        lat: Double,
        long: Double,
        kelurahan: String,
        kecamatan :String,
        address: String
    ): Response<GeneralApiResponse> {
        return restoService.updateRestoAddress(
            latitude = lat,
            longitude = long,
            address = address,
            restoId = id,
            kecamatan = kecamatan,
            kelurahan = kelurahan
        )
    }


     suspend fun getFoodCategoryOnResto(id: String): Response<FoodCategoryResponse> {
        return restoService.getFoodCategoryOnResto(id)
    }

     suspend fun getFoodByCategory(id: String): Response<AllFoodByRestoResponse> {
        return restoService.getFoodByCategory(id)
    }

     suspend fun getAllFoodByResto(id: String): Response<AllFoodByRestoResponse> {
        return restoService.getAllFoodOnResto(id)
    }

     suspend fun getMyResto(): Response<AllRestoNoPagination> {
        return restoService.getMyRestaurant()
    }

     suspend fun addFavoriteMasjid(masjidId: String): Response<AddFavMasjidResponse> {
        return favoriteService.addFavoriteMasjid(masjidId)
    }

    suspend fun deleteFavMasjid(id: String) = favoriteService.deleteFavoriteMasjid(id)
    suspend fun deleteFavResto(id: String) = favoriteService.deleteFavoriteResto(id)

     suspend fun addFavoriteResto(restoId: String): Response<AddFavRestoResponse> {
        return favoriteService.addFavoriteResto(restoId)
    }

     suspend fun createForum(body: RequestBody): Response<CreateForumResponse?>? {
        return forumService.createForum(body)
    }

     suspend fun updateForum(
        forumId: String,
        body: RequestBody,
        isDeletingImage: Boolean
    ): Response<CreateForumResponse?>? {
        return forumService.updateForum(
            forumId.toIntOrNull() ?: 0,
            body,
            isDeletingImage = isDeletingImage
        )
    }

     suspend fun getForumCategory(): Response<ForumCategoryResponse> {
        return forumService.getForumsCategory()
    }

     suspend fun getAllForumPaginate(
        page: Int,
        perPage: Int
    ): Response<AllForumPaginationResponse> {
        return forumService.getAllForum(perPage = perPage, page = page)
    }

     suspend fun likeForum(forumId: Int): Response<GeneralApiResponse> {
        return forumService.likeForum(forumId)
    }

     suspend fun unlikeForum(forumId: Int): Response<GeneralApiResponse> {
        return forumService.unlikeForum(forumId)
    }

     suspend fun likeComment(forumId: Int): Response<GeneralApiResponse> {
        return forumService.likeCommentForum(forumId)
    }

     suspend fun unlikeComment(forumId: Int): Response<GeneralApiResponse> {
        return forumService.unlikeComment(forumId)
    }

     suspend fun deleteForum(forumId: Int): Response<GeneralApiResponse> {
        return forumService.deleteForum(forumId)
    }

     suspend fun deleteComment(commentId: Int): Response<GeneralApiResponse> {
        return forumService.deleteComment(commentId)
    }

     suspend fun createComment(body: CreateCommentPayload): Response<AddCommentResponse> {
        return forumService.createComment(body)
    }

     suspend fun getDetailForum(forumId: Int): Response<ForumDetailResponse> {
        return forumService.detailForum(forumId)
    }

     suspend fun getCommentForum(forumId: Int): Response<ForumCommentResponse> {
        return forumService.commentOnForum(forumId)
    }


    //    ORDER
     suspend fun checkout(body: CreateCartPayload): Response<CreateCartResponse> {
        return restoService.createCart(body = body, restoId = body.restoId)
    }

     suspend fun orderReject(orderId: Int, reason: String): Response<GeneralApiResponse> {
        return restoService.orderReject(orderId = orderId.toString(), reason = reason)
    }

     suspend fun orderApprove(orderId: Int): Response<GeneralApiResponse> {
        return restoService.orderApprove(orderId = orderId.toString())
    }

     suspend fun orderDelivered(orderId: Int, driverId: Int): Response<GeneralApiResponse> {
        return restoService.orderDelivered(orderId = orderId.toString(), driverId)
    }

     suspend fun orderFinished(
        orderId: String,
        body: RequestBody
    ): Response<DetailOrderResponse?>? {
        return restoService.orderFinish(body = body, orderId)
    }

     suspend fun orderDetail(orderId: Int): Response<DetailOrderResponse> {
        return restoService.getOrderDetail(orderId = orderId.toString())
    }

     suspend fun getHistoryOrder(): Response<OrderHistoryResponse> {
        return restoService.getUserOrderHistory()
    }

     suspend fun getOrderStatus() = restoService.getAllOrderStatus()
     suspend fun getRestoHistoryOrder(
        restoId: String,
        page: Int, perPage: Int,
        mStatus: String?
    ): Response<OrderByRestoPaginationResponse> {
        mStatus?.let {
            return restoService.getRestoOrder(
                restoId = restoId,
                page = 1,
                perPage = 10,
                status = mStatus
            )
        } ?: run {
            return restoService.getRestoOrder(
                restoId = restoId,
                page = 1,
                perPage = 10,
                status = null
            )
        }

    }

     suspend fun geDriverOnResto(restoId: String): Response<GetAllDriverResponse> {
        return restoService.getAllDriverOnResto(restoId)
    }

     suspend fun deleteDriver(driverId: String): Response<GeneralApiResponse> {
        return restoService.deleteDriver(driverId)
    }

     suspend fun addNewDriverByResto(
        image: RequestBody?,
        phoneNumber: String,
        email: String,
        name: String,
        password: String
    ) = restoService.addNewDriver(
        password = password,
        confirm_password = password,
        name = name, email = email, phone_number = phoneNumber,
        file = image
    )

     suspend fun editDriver(
        image: RequestBody?,
        driverId: String,
        phoneNumber: String,
        email: String,
        name: String
    ) = commonService.editDriver(
        driverId = driverId.toString(),
        file = image, phone_number = phoneNumber, email = email, name = name
    )


    //driver section
     suspend fun getDriverOrder(
        page: Int,
        perPage: Int,
    ): Response<DriverOrderPaginationResponse> {
        return driverService.getOrderByDriver(page = page, perPage = perPage)
    }
}
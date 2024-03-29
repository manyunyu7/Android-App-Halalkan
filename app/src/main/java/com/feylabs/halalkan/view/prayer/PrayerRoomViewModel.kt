package com.feylabs.halalkan.view.prayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.MasjidRepository
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.masjid.*
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.repository.PrayerTimeRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

class PrayerRoomViewModel(
    val repo: QumparanRepository,
    val masjidRepository: MasjidRepository,
    val prayerTimeRepository: PrayerTimeRepository,
    val ds: RemoteDataSource
) : ViewModel() {

    val targetLat: MutableLiveData<Double> = MutableLiveData(-99.0)
    val targetLong: MutableLiveData<Double> = MutableLiveData(-99.0)

    private var _masjidLiveData =
        MutableLiveData<QumparanResource<MasjidResponseWithoutPagination?>>()
    val masjidLiveData get() = _masjidLiveData

    private var _searchMasjidLiveData =
        MutableLiveData<QumparanResource<MasjidSearchResponse?>>()
    val searchMasjidLiveData get() = _searchMasjidLiveData

    private var _masjidTypeLiveData =
        MutableLiveData<QumparanResource<MasjidTypeResponse?>>()
    val masjidTypeLiveData get() = _masjidTypeLiveData

    private var _masjidPaginateLiveData =
        MutableLiveData<QumparanResource<AllMasjidPaginationResponse?>>()
    val masjidPaginateLiveData get() = _masjidPaginateLiveData

    private var _prayerTimeSingleLiveData:
            MutableLiveData<QumparanResource<PrayerTimeAladhanSingleDateResponse?>> =
        MutableLiveData()
    val prayerTimeSingleLiveData get() = _prayerTimeSingleLiveData

    private var _masjidReviewsLiveData =
        MutableLiveData<QumparanResource<MasjidReviewPaginationResponse?>>()
    val masjidReviewsLiveData get() = _masjidReviewsLiveData


    private var _masjidDetailLiveData =
        MutableLiveData<QumparanResource<MasjidDetailResponse?>>()
    val masjidDetailLiveData get() = _masjidDetailLiveData


    private var _masjidPhotoLiveData =
        MutableLiveData<QumparanResource<MasjidPhotosResponse?>>()
    val masjidPhotoLiveData get() = _masjidPhotoLiveData

    fun searchMasjid(
        typeId: Int? = null,
        name: String? = null,
        page: Int = 1,
        perPage: Int? = null,
        sortBy:String? = null
    ) {
        _searchMasjidLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.searchMasjid(
                    name = name,
                    typeId = typeId,
                    page = page,
                    perPage = perPage,
                    sortBy = sortBy
                )
                if (res.isSuccessful) {
                    _searchMasjidLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _searchMasjidLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _searchMasjidLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getMasjidWithPagination(page: Int) {
        _masjidPaginateLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = masjidRepository.getMasjidPaginate(page)
                if (res.isSuccessful) {
                    _masjidPaginateLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidPaginateLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _masjidPaginateLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getMasjidType() {
        _masjidTypeLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getMasjidType()
                if (res.isSuccessful) {
                    _masjidTypeLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidTypeLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _masjidTypeLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getMasjidPhoto(id: String) {
        viewModelScope.launch {
            try {
                val res = masjidRepository.getMasjidPhotos(id)
                if (res.isSuccessful) {
                    _masjidPhotoLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidPhotoLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _masjidLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun fetchPrayerTimeSingle(
        latitude: Double,
        longitude: Double,
        method: String,
        time: String
    ) {
        viewModelScope.launch {
            try {
                val res = prayerTimeRepository.getPrayerTimeSingleDate(
                    lat = latitude.toString(),
                    long = longitude.toString(),
                    method = method,
                    time = time
                )
                if (res.isSuccessful) {
                    _prayerTimeSingleLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _prayerTimeSingleLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _prayerTimeSingleLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getDetailMasjid(id: String) {
        viewModelScope.launch {
            _masjidDetailLiveData.postValue(QumparanResource.Loading())
            try {
                val res = masjidRepository.getMasjidDetail(id)
                if (res.isSuccessful) {
                    _masjidDetailLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidDetailLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _masjidDetailLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


    fun getAllMosque() {
        viewModelScope.launch {
            try {
                val res = repo.getMasjids()
                if (res.isSuccessful) {
                    _masjidLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidLiveData.postValue(QumparanResource.Error(res.errorBody().toString()))
                }
            } catch (e: Exception) {
                _masjidLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getMasjidReview(masjidId: String, page: Int, perPage: Int=5) {
        viewModelScope.launch {
            try {
                _masjidReviewsLiveData.postValue(QumparanResource.Loading())
                val res = ds.getMasjidReviews(masjidId, page = page, perPage = perPage)
                if (res.isSuccessful) {
                    _masjidReviewsLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidReviewsLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _masjidReviewsLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

}
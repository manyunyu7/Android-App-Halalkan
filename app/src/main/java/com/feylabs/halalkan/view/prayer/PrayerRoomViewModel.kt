package com.feylabs.halalkan.view.prayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.MasjidRepository
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidDetailResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.repository.PrayerTimeRepository
import kotlinx.coroutines.launch

class PrayerRoomViewModel(
    val repo: QumparanRepository,
    val masjidRepository: MasjidRepository,
    val prayerTimeRepository: PrayerTimeRepository,
    val ds: RemoteDataSource
) : ViewModel() {

    val targetLat: MutableLiveData<Double> = MutableLiveData(-99.0)
    val targetLong: MutableLiveData<Double> = MutableLiveData(-99.0)

    private var _masjidLiveData =
        MutableLiveData<MuskoResource<MasjidResponseWithoutPagination?>>()
    val masjidLiveData get() = _masjidLiveData

    private var _masjidPaginateLiveData =
        MutableLiveData<MuskoResource<AllMasjidPaginationResponse?>>()
    val masjidPaginateLiveData get() = _masjidPaginateLiveData

    private var _prayerTimeSingleLiveData:
            MutableLiveData<MuskoResource<PrayerTimeAladhanSingleDateResponse?>> =
        MutableLiveData()
    val prayerTimeSingleLiveData get() = _prayerTimeSingleLiveData

    private var _masjidReviewsLiveData =
        MutableLiveData<MuskoResource<MasjidReviewPaginationResponse?>>()
    val masjidReviewsLiveData get() = _masjidReviewsLiveData


    private var _masjidDetailLiveData =
        MutableLiveData<MuskoResource<MasjidDetailResponse?>>()
    val masjidDetailLiveData get() = _masjidDetailLiveData


    private var _masjidPhotoLiveData =
        MutableLiveData<MuskoResource<MasjidPhotosResponse?>>()
    val masjidPhotoLiveData get() = _masjidPhotoLiveData

    fun getMasjidWithPagination(page: Int) {
        _masjidPaginateLiveData.postValue(MuskoResource.Loading())
        viewModelScope.launch {
            try {
                val res = masjidRepository.getMasjidPaginate(page)
                if (res.isSuccessful) {
                    _masjidPaginateLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _masjidPaginateLiveData.postValue(
                        MuskoResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _masjidPaginateLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun getMasjidPhoto(id: String) {
        viewModelScope.launch {
            try {
                val res = masjidRepository.getMasjidPhotos(id)
                if (res.isSuccessful) {
                    _masjidPhotoLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _masjidPhotoLiveData.postValue(
                        MuskoResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _masjidLiveData.postValue(MuskoResource.Error(e.message.toString()))
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
                    _prayerTimeSingleLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _prayerTimeSingleLiveData.postValue(MuskoResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _prayerTimeSingleLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun getDetailMasjid(id: String) {
        viewModelScope.launch {
            _masjidDetailLiveData.postValue(MuskoResource.Loading())
            try {
                val res = masjidRepository.getMasjidDetail(id)
                if (res.isSuccessful) {
                    _masjidDetailLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _masjidDetailLiveData.postValue(
                        MuskoResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _masjidDetailLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }


    fun getAllMosque() {
        viewModelScope.launch {
            try {
                val res = repo.getMasjids()
                if (res.isSuccessful) {
                    _masjidLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _masjidLiveData.postValue(MuskoResource.Error(res.errorBody().toString()))
                }
            } catch (e: Exception) {
                _masjidLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun getMasjidReview(masjidId: String, page: Int, perPage: Int=5) {
        viewModelScope.launch {
            try {
                _masjidReviewsLiveData.postValue(MuskoResource.Loading())
                val res = ds.getMasjidReviews(masjidId, page = page, perPage = perPage)
                if (res.isSuccessful) {
                    _masjidReviewsLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _masjidReviewsLiveData.postValue(
                        MuskoResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _masjidReviewsLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

}
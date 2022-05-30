package com.feylabs.halalkan.view.prayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.MasjidRepository
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidDetailResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewsResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.repository.PrayerTimeRepository
import kotlinx.coroutines.launch
import javax.sql.DataSource

class PrayerRoomViewModel(
    val repo: QumparanRepository,
    val masjidRepository: MasjidRepository,
    val prayerTimeRepository : PrayerTimeRepository,
    val ds : RemoteDataSource
) : ViewModel() {

    private var _masjidLiveData =
        MutableLiveData<QumparanResource<MasjidResponseWithoutPagination?>>()
    val masjidLiveData get() = _masjidLiveData

    private var _prayerTimeSingleLiveData:
            MutableLiveData<QumparanResource<PrayerTimeAladhanSingleDateResponse?>> =
        MutableLiveData()
    val prayerTimeSingleLiveData get() = _prayerTimeSingleLiveData

    private var _masjidReviewsLiveData =
        MutableLiveData<QumparanResource<MasjidReviewsResponse?>>()
    val masjidReviewsLiveData get() = _masjidReviewsLiveData


    private var _masjidDetailLiveData =
        MutableLiveData<QumparanResource<MasjidDetailResponse?>>()
    val masjidDetailLiveData get() = _masjidDetailLiveData


    private var _masjidPhotoLiveData =
        MutableLiveData<QumparanResource<MasjidPhotosResponse?>>()
    val masjidPhotoLiveData get() = _masjidPhotoLiveData

    fun getMasjidPhoto(id:String){
        viewModelScope.launch {
            try {
                val res = masjidRepository.getMasjidPhotos(id)
                if (res.isSuccessful) {
                    _masjidPhotoLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidPhotoLiveData.postValue(QumparanResource.Error(res.errorBody().toString()))
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

    fun getDetailMasjid(id:String){
        viewModelScope.launch {
            _masjidDetailLiveData.postValue(QumparanResource.Loading())
            try {
                val res = masjidRepository.getMasjidDetail(id)
                if (res.isSuccessful) {
                    _masjidDetailLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidDetailLiveData.postValue(QumparanResource.Error(res.errorBody().toString()))
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

    fun getMasjidReview(masjidId:String) {
        viewModelScope.launch {
            try {
                _masjidReviewsLiveData.postValue(QumparanResource.Loading())
                val res = ds.getMasjidReviews(masjidId)
                if (res.isSuccessful) {
                    _masjidReviewsLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidReviewsLiveData.postValue(QumparanResource.Error(res.errorBody().toString()))
                }
            } catch (e: Exception) {
                _masjidReviewsLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

}
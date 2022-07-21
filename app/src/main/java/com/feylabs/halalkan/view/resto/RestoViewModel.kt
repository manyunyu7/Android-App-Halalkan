package com.feylabs.halalkan.view.resto

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
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.FoodTypeResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestaurantCertificationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.data.repository.PrayerTimeRepository
import kotlinx.coroutines.launch

class RestoViewModel(
    val ds: RemoteDataSource
) : ViewModel() {


    // for mapbox direction
    val targetLat: MutableLiveData<Double> = MutableLiveData(-99.0)
    val targetLong: MutableLiveData<Double> = MutableLiveData(-99.0)

    private var _certLiveData =
        MutableLiveData<QumparanResource<RestaurantCertificationResponse?>>()
    val certLiveData get() = _certLiveData

    private var _foodTypeLiveData =
        MutableLiveData<QumparanResource<FoodTypeResponse?>>()
    val foodTypeLiveData get() = _foodTypeLiveData

    private var _allRestoRawLiveData =
        MutableLiveData<QumparanResource<AllRestoNoPagination?>>()
    val allRestoRawLiveData get() = _allRestoRawLiveData

    private var _detailRestoLiveData =
        MutableLiveData<QumparanResource<RestoDetailResponse?>>()
    val detailRestoLiveData get() = _detailRestoLiveData

    fun getAllRestoRaw() {
        _allRestoRawLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoAll()
                if (res.isSuccessful) {
                    _allRestoRawLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _allRestoRawLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _allRestoRawLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getDetailResto(id:String) {
        _detailRestoLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoDetail(id)
                if (res.isSuccessful) {
                    _detailRestoLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _detailRestoLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _detailRestoLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


    fun getRestoCert() {
        _certLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoCert()
                if (res.isSuccessful) {
                    _certLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _certLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _certLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getFoodType() {
        _foodTypeLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getFoodType()
                if (res.isSuccessful) {
                    _foodTypeLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _foodTypeLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _foodTypeLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


}
package com.feylabs.halalkan.view.resto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.FoodTypeResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestaurantCertificationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.data.remote.reqres.resto.food.RestoFoodByCommonCategoryResponse
import kotlinx.coroutines.launch

class RestoViewModel(
    val ds: RemoteDataSource
) : ViewModel() {

    // for mapbox direction
    val targetLat: MutableLiveData<Double> = MutableLiveData(-99.0)
    val targetLong: MutableLiveData<Double> = MutableLiveData(-99.0)

    val currentMenuTab: MutableLiveData<Pair<Int, String>> = MutableLiveData()

    private var _certLiveData =
        MutableLiveData<MuskoResource<RestaurantCertificationResponse?>>()
    val certLiveData get() = _certLiveData

    private var _foodTypeLiveData =
        MutableLiveData<MuskoResource<FoodTypeResponse?>>()
    val foodTypeLiveData get() = _foodTypeLiveData

    private var _restoFoodByCategoryLiveData =
        MutableLiveData<MuskoResource<RestoFoodByCommonCategoryResponse?>>()
    val restoFoodByCategoryLiveData get() = _restoFoodByCategoryLiveData

    private var _allRestoRawLiveData =
        MutableLiveData<MuskoResource<AllRestoNoPagination?>>()
    val allRestoRawLiveData get() = _allRestoRawLiveData

    private var _detailRestoLiveData =
        MutableLiveData<MuskoResource<RestoDetailResponse?>>()
    val detailRestoLiveData get() = _detailRestoLiveData

    fun getAllRestoRaw() {
        _allRestoRawLiveData.postValue(MuskoResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoAll()
                if (res.isSuccessful) {
                    _allRestoRawLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _allRestoRawLiveData.postValue(
                        MuskoResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _allRestoRawLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun getRestoFoodByCategory(restoId: String, categoryId: String) {
        _restoFoodByCategoryLiveData.postValue(MuskoResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoFoodByCommonCategory(restoId, categoryId)
                if (res.isSuccessful) {
                    _restoFoodByCategoryLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _restoFoodByCategoryLiveData.postValue(
                        MuskoResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _restoFoodByCategoryLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun getDetailResto(id: String) {
        _detailRestoLiveData.postValue(MuskoResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoDetail(id)
                if (res.isSuccessful) {
                    _detailRestoLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _detailRestoLiveData.postValue(
                        MuskoResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _detailRestoLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun getRestoCert() {
        _certLiveData.postValue(MuskoResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoCert()
                if (res.isSuccessful) {
                    _certLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _certLiveData.postValue(
                        MuskoResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _certLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun getFoodType() {
        _foodTypeLiveData.postValue(MuskoResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getFoodType()
                if (res.isSuccessful) {
                    _foodTypeLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _foodTypeLiveData.postValue(
                        MuskoResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _foodTypeLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }


}
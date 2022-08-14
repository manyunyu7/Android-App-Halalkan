package com.feylabs.halalkan.view.resto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.resto.*
import kotlinx.coroutines.launch

class RestoViewModel(
    val ds: RemoteDataSource
) : ViewModel() {

    // for mapbox direction
    val targetLat: MutableLiveData<Double> = MutableLiveData(-99.0)
    val targetLong: MutableLiveData<Double> = MutableLiveData(-99.0)

    val currentMenuTab: MutableLiveData<Pair<Int, String>> = MutableLiveData()

    private var _certLiveData =
        MutableLiveData<QumparanResource<RestaurantCertificationResponse?>>()
    val certLiveData get() = _certLiveData

    private var _foodCategoryLiveData =
        MutableLiveData<QumparanResource<FoodCategoryResponse?>>()
    val foodCategoryLiveData get() = _foodCategoryLiveData

    private var _foodByCategoryLiveData =
        MutableLiveData<QumparanResource<AllFoodByRestoResponse?>>()
    val foodByCategoryLiveData get() = _foodByCategoryLiveData

    //saved
    private var _allFoodLiveData =
        MutableLiveData<QumparanResource<AllFoodByRestoResponse?>>()
    val allFoodLiveData get() = _allFoodLiveData

    private var _foodTypeLiveData =
        MutableLiveData<QumparanResource<FoodTypeResponse?>>()
    val foodTypeLiveData get() = _foodTypeLiveData

    private var _restoFoodByCategoryLiveData =
        MutableLiveData<QumparanResource<AllFoodByRestoResponse?>>()
    val restoFoodByCategoryLiveData get() = _restoFoodByCategoryLiveData

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

    fun getRestoFoodByCategory(restoId: String, categoryId: String) {
        _restoFoodByCategoryLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getRestoFoodByCommonCategory(restoId, categoryId)
                if (res.isSuccessful) {
                    _restoFoodByCategoryLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _restoFoodByCategoryLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _restoFoodByCategoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getDetailResto(id: String) {
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

    fun getFoodCategoryOnResto(restoId:String){
        _foodCategoryLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getFoodCategoryOnResto(restoId)
                if (res.isSuccessful) {
                    _foodCategoryLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _foodCategoryLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _foodCategoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getAllFoodByResto(restoId: String){
        viewModelScope.launch {
            try {
                val res = ds.getAllFoodByResto(restoId)
                if (res.isSuccessful) {
                    _allFoodLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _allFoodLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _allFoodLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


}
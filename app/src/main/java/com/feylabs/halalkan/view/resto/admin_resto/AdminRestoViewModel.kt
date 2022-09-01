package com.feylabs.halalkan.view.resto.admin_resto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateRestoColumnResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File

class AdminRestoViewModel(
    val ds: RemoteDataSource
) : ViewModel() {

    // for mapbox direction
    val targetLat: MutableLiveData<Double> = MutableLiveData(-99.0)
    val targetLong: MutableLiveData<Double> = MutableLiveData(-99.0)

    val currentMenuTab: MutableLiveData<Pair<Int, String>> = MutableLiveData()

    private var _certLiveData =
        MutableLiveData<QumparanResource<RestaurantCertificationResponse?>>()
    val certLiveData get() = _certLiveData

    private var _updateRestoColumn =
        MutableLiveData<QumparanResource<UpdateRestoColumnResponse?>>()
    val updateRestoColumnLiveData get() = _updateRestoColumn

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

    private var _myRestoLiveData =
        MutableLiveData<QumparanResource<AllRestoNoPagination?>>()
    val myRestoLiveData get() = _myRestoLiveData

    private var _detailRestoLiveData =
        MutableLiveData<QumparanResource<RestoDetailResponse?>>()
    val detailRestoLiveData get() = _detailRestoLiveData

    private var _createRestoLiveData =
        MutableLiveData<QumparanResource<ResponseBody?>>()
    val createRestoLiveData get() = _createRestoLiveData

    private var _createRestoFoodCategoryLiveData =
        MutableLiveData<QumparanResource<ResponseBody?>>()
    val createRestoFoodCategoryLiveData get() = _createRestoFoodCategoryLiveData

    fun addFoodCategoryForResto(
        name: String,
        restoId: String,
    ) {
        viewModelScope.launch {
            _createRestoFoodCategoryLiveData.postValue(QumparanResource.Loading())
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("name", name)
            val requestBody: MultipartBody = builder.build()
            try {
                val req = ds.createRestoFoodCategory(restoId,requestBody)
                req?.let {
                    if (req.isSuccessful) {
                        _createRestoFoodCategoryLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _createRestoFoodCategoryLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _createRestoFoodCategoryLiveData.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _createRestoFoodCategoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun addRestaurant(
        body: SaveRestoPayload,
        file:File
    ) {
        viewModelScope.launch {
            _createRestoLiveData.postValue(QumparanResource.Loading())
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("name", body.name)
            builder.addFormDataPart("type_food_id", body.typeFoodId)
            builder.addFormDataPart("certification_id", body.certificationId)
            builder.addFormDataPart("description", body.description)
            builder.addFormDataPart("phone_number", body.phoneNumber)
            builder.addFormDataPart("lat", body.lat)
            builder.addFormDataPart("long", body.long)
            builder.addFormDataPart("is_visible", "1")
            builder.addFormDataPart("address", body.address)
            builder.addFormDataPart(
                "image",
                file.name,
                RequestBody.create(("multipart/form-data").toMediaTypeOrNull(), file)
            )
            val requestBody: MultipartBody = builder.build()

            try {
                val req = ds.createResto(requestBody)
                req?.let {
                    if (req.isSuccessful) {
                        _createRestoLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _createRestoLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _createRestoLiveData.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _createRestoLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getMyResto() {
        _myRestoLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getMyResto()
                if (res.isSuccessful) {
                    _myRestoLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _myRestoLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _myRestoLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

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

    fun updateRestoColumn(restoId: String, name:String, updatepath:String, columnValue:String) {
        viewModelScope.launch {
            _updateRestoColumn.postValue(QumparanResource.Loading())
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart(name, columnValue)
            val requestBody: MultipartBody = builder.build()
            try {
                val req = ds.updateRestoColumn(
                    id = restoId, pathupdate = updatepath, body = requestBody
                )
                req?.let {
                    if (req.isSuccessful) {
                        _updateRestoColumn.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _updateRestoColumn.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _updateRestoColumn.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _updateRestoColumn.postValue(QumparanResource.Error(e.message.toString()))
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
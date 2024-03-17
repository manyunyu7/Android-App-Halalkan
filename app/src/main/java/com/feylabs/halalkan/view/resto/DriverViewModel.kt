package com.feylabs.halalkan.view.resto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import com.feylabs.halalkan.data.remote.reqres.driver.GetAllDriverResponse
import com.feylabs.halalkan.data.remote.reqres.order.DriverOrderPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateRestoColumnResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File

class DriverViewModel(
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

    private var _updateDriverLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val updateDriverLiveData get() = _updateDriverLiveData

    private var _addNewDriverLiveData =
        MutableLiveData<QumparanResource<RegisterResponse?>>()
    val addNewDriverLiveData get() = _addNewDriverLiveData

    private var _deleteDriverLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val deleteDriverLiveData get() = _deleteDriverLiveData

    private var _driverOnRestoLiveData =
        MutableLiveData<QumparanResource<GetAllDriverResponse?>>()
    val driverOnRestoLiveData get() = _driverOnRestoLiveData

    private var _driverOrderLiveData =
        MutableLiveData<QumparanResource<DriverOrderPaginationResponse?>>()
    val driverOrderLiveData get() = _driverOrderLiveData

    fun getDriverOrder(page: Int = 1, perPage: Int = 999999) {
        _driverOrderLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getDriverOrder(page = page, perPage = perPage)
                if (res.isSuccessful) {
                    _driverOrderLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _driverOrderLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _driverOrderLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getDriverOnResto(restoId: String) {
        _driverOnRestoLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.geDriverOnResto(restoId)
                if (res.isSuccessful) {
                    _driverOnRestoLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _driverOnRestoLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _driverOnRestoLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun deleteDriver(driverId: String) {
        _deleteDriverLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.deleteDriver(driverId)
                if (res.isSuccessful) {
                    _deleteDriverLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _deleteDriverLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _deleteDriverLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun addNewDriver(file: File?, registerBodyRequest: RegisterBodyRequest) {
        _addNewDriverLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            val fBody: RequestBody? = file?.asRequestBody("image/jpeg".toMediaTypeOrNull())
            try {
                val req = ds.addNewDriverByResto(
                    name = registerBodyRequest.name,
                    email = registerBodyRequest.email,
                    phoneNumber = registerBodyRequest.phoneNumber,
                    image = fBody,
                    password = registerBodyRequest.password,
                )
                req?.let {
                    if (req.isSuccessful) {
                        _addNewDriverLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _addNewDriverLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _addNewDriverLiveData.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _addNewDriverLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun updateDriver(
        driverId: String,
        driverName: String,
        phone: String,
        email: String,
        file: File?
    ) {
        viewModelScope.launch {
            _updateDriverLiveData.postValue(QumparanResource.Loading())
            val fBody: RequestBody? = file?.asRequestBody("image/jpeg".toMediaTypeOrNull())
            try {
                val req = ds.editDriver(
                    driverId = driverId,
                    name = driverName,
                    email = email,
                    phoneNumber = phone,
                    image = fBody
                )
                req?.let {
                    if (req.isSuccessful) {
                        _updateDriverLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _updateDriverLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _updateDriverLiveData.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _updateDriverLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


}
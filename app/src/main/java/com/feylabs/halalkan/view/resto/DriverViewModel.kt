package com.feylabs.halalkan.view.resto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import com.feylabs.halalkan.data.remote.reqres.driver.GetAllDriverResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import com.feylabs.halalkan.data.remote.reqres.resto.update.UpdateRestoColumnResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


    private var _addNewDriverLiveData =
        MutableLiveData<QumparanResource<RegisterResponse?>>()
    val addNewDriverLiveData get() = _addNewDriverLiveData


    private var _driverOnRestoLiveData =
        MutableLiveData<QumparanResource<GetAllDriverResponse?>>()
    val driverOnRestoLiveData get() = _driverOnRestoLiveData

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

    fun addNewDriver(registerBodyRequest: RegisterBodyRequest) {
        _addNewDriverLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.addNewDriverByResto(registerBodyRequest)
                if (res.isSuccessful) {
                    val body = res.body()
                    body?.let { registerResponse ->
                        if (registerResponse.code > 299 || registerResponse.code < 200) {
                            _addNewDriverLiveData.postValue(QumparanResource.Error(registerResponse.message))
                        } else {
                            _addNewDriverLiveData.postValue(QumparanResource.Success(registerResponse,registerResponse.code.toString()))
                        }
                    } ?: run{
                        _addNewDriverLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                    }
                } else {
                    _addNewDriverLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _addNewDriverLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }




}
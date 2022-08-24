package com.feylabs.halalkan.view.resto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartPayload
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartResponse
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryResponse
import com.feylabs.halalkan.data.remote.reqres.order.resto.OrderByRestoPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import kotlinx.coroutines.launch
import org.json.JSONObject

class OrderViewModel(
    val ds: RemoteDataSource
) : ViewModel() {

    private var _checkoutLiveData =
        MutableLiveData<QumparanResource<CreateCartResponse?>>()
    val checkoutLiveData get() = _checkoutLiveData

    private var _orderHistoryLiveData =
        MutableLiveData<QumparanResource<OrderHistoryResponse?>>()
    val orderHistoryLiveData get() = _orderHistoryLiveData

    private var _restoHistoryLiveData =
        MutableLiveData<QumparanResource<OrderByRestoPaginationResponse?>>()
    val restoHistoryLiveData get() = _restoHistoryLiveData

    fun checkout(payload: CreateCartPayload) {
        viewModelScope.launch {
            try {
                val res = ds.checkout(payload)
                if (res.isSuccessful) {
                    _checkoutLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _checkoutLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _checkoutLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getRestoHistory(
        restoId: String,
        page: Int,
        perPage: Int
    ) {
        viewModelScope.launch {
            _restoHistoryLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.getRestoHistoryOrder(
                    restoId = restoId,
                    page = page,
                    perPage = perPage
                )
                if (res.isSuccessful) {
                    _restoHistoryLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _restoHistoryLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                throw e
                _restoHistoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getOrderHistory() {
        viewModelScope.launch {
            _orderHistoryLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.getHistoryOrder()
                if (res.isSuccessful) {
                    _orderHistoryLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _orderHistoryLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                throw e
                _orderHistoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


}
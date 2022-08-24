package com.feylabs.halalkan.view.resto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartPayload
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartResponse
import com.feylabs.halalkan.data.remote.reqres.order.DetailOrderResponse
import com.feylabs.halalkan.data.remote.reqres.order.OrderStatusResponse
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryResponse
import com.feylabs.halalkan.data.remote.reqres.order.resto.OrderByRestoPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import kotlinx.coroutines.launch
import org.json.JSONObject

class OrderViewModel(
    val ds: RemoteDataSource
) : ViewModel() {

    var currentOrderFilterLiveData = MutableLiveData("")

    private var _checkoutLiveData =
        MutableLiveData<QumparanResource<CreateCartResponse?>>()
    val checkoutLiveData get() = _checkoutLiveData

    private var _orderHistoryLiveData =
        MutableLiveData<QumparanResource<OrderHistoryResponse?>>()
    val orderHistoryLiveData get() = _orderHistoryLiveData

    private var _rejectOrderLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val rejectOrderLiveData get() = _rejectOrderLiveData

    private var _successOrderLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val successOrderLiveData get() = _successOrderLiveData

    private var _approveOrderLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val approveOrderLiveData get() = _approveOrderLiveData

    private var _statusOrderLiveData =
        MutableLiveData<QumparanResource<OrderStatusResponse?>>()
    val statusOrderLiveData get() = _statusOrderLiveData

    private var _detailOrderLiveData =
        MutableLiveData<QumparanResource<DetailOrderResponse?>>()
    val detailOrderLiveData get() = _detailOrderLiveData

    private var _restoHistoryLiveData =
        MutableLiveData<QumparanResource<OrderByRestoPaginationResponse?>>()
    val restoHistoryLiveData get() = _restoHistoryLiveData

    fun getCurrentFilterStatus(): String {
        return currentOrderFilterLiveData.value.orEmpty()
    }

    fun checkout(payload: CreateCartPayload) {
        _checkoutLiveData.postValue(QumparanResource.Loading())
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

    fun getDetailOrder(orderId: Int) {
        _detailOrderLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.orderDetail(orderId)
                if (res.isSuccessful) {
                    _detailOrderLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _detailOrderLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _detailOrderLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun rejectOrder(orderId: Int, reason: String) {
        _rejectOrderLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.orderReject(orderId, reason)
                if (res.isSuccessful) {
                    _rejectOrderLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _rejectOrderLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _rejectOrderLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun approveOrder(orderId: Int) {
        _approveOrderLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.orderApprove(orderId)
                if (res.isSuccessful) {
                    _approveOrderLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _approveOrderLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _approveOrderLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getAllOrderStatusLiveData() {
        _statusOrderLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getOrderStatus()
                if (res.isSuccessful) {
                    _statusOrderLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _statusOrderLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _statusOrderLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getRestoHistory(
        restoId: String,
        page: Int = 1,
        perPage: Int = 10,
        status: String? = null
    ) {
        viewModelScope.launch {
            _restoHistoryLiveData.postValue(QumparanResource.Loading())
            try {
                val res = ds.getRestoHistoryOrder(
                    restoId = restoId,
                    page = page,
                    perPage = perPage,
                    mStatus = status
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

    fun resetAcceptOrderState() {
        _approveOrderLiveData.postValue(QumparanResource.Default())
    }

    fun resetRejectOrderState() {
        _rejectOrderLiveData.postValue(QumparanResource.Default())
    }


}
package com.feylabs.halalkan.view.resto

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartPayload
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartResponse
import com.feylabs.halalkan.data.remote.reqres.resto.*
import kotlinx.coroutines.launch

class OrderViewModel(
    val ds: RemoteDataSource
) : ViewModel() {

    private var _checkoutLiveData =
        MutableLiveData<QumparanResource<CreateCartResponse?>>()
    val checkoutLiveData get() = _checkoutLiveData

    fun checkout(payload: CreateCartPayload){
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


}
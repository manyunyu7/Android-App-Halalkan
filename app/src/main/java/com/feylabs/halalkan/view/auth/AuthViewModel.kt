package com.feylabs.halalkan.view.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.AlbumPhotoResponse
import com.feylabs.halalkan.data.remote.reqres.PostCommentResponse
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.LoginResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModel(val repo: QumparanRepository, ds: RemoteDataSource) : ViewModel() {

    private var _loginLiveData = MutableLiveData<QumparanResource<LoginResponse?>>()
    val loginLiveData get() = _loginLiveData

    private var _registerLiveData = MutableLiveData<QumparanResource<RegisterResponse?>>()
    val registerLiveData get() = _registerLiveData

    fun login(loginBodyRequest: LoginBodyRequest) {
        _loginLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = repo.login(loginBodyRequest)
                Timber.d("users response $")
                if (res.isSuccessful) {
                    _loginLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _loginLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _loginLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun register(registerBodyRequest: RegisterBodyRequest) {
        _registerLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = repo.register(registerBodyRequest)
                if (res.isSuccessful) {
                    _registerLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _registerLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _registerLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

}
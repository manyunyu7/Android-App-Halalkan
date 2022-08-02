package com.feylabs.halalkan.view.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.LoginResponse
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthViewModel(val repo: QumparanRepository, ds: RemoteDataSource) : ViewModel() {

    private var _loginLiveData = MutableLiveData<MuskoResource<LoginResponse?>>()
    val loginLiveData get() = _loginLiveData

    private var _registerLiveData = MutableLiveData<MuskoResource<RegisterResponse?>>()
    val registerLiveData get() = _registerLiveData

    fun login(loginBodyRequest: LoginBodyRequest) {
        _loginLiveData.postValue(MuskoResource.Loading())
        viewModelScope.launch {
            try {
                val res = repo.login(loginBodyRequest)
                Timber.d("users response $")
                if (res.isSuccessful) {

                    val body = res.body()
                    body?.let { loginResponse ->
                        if (loginResponse.mCode > 299 || loginResponse.mCode < 200) {
                            _loginLiveData.postValue(MuskoResource.Error(message =  loginResponse.mMessage.orEmpty() + " (Musko)"))
                        } else {
                            _loginLiveData.postValue(MuskoResource.Success(data = loginResponse, message = loginResponse.mMessage.toString()))
                        }
                    }
                } else {
                    _loginLiveData.postValue(MuskoResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _loginLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun register(registerBodyRequest: RegisterBodyRequest) {
        _registerLiveData.postValue(MuskoResource.Loading())
        viewModelScope.launch {
            try {
                val res = repo.register(registerBodyRequest)
                if (res.isSuccessful) {

                    val body = res.body()
                    body?.let { registerResponse ->
                        if (registerResponse.code > 299 || registerResponse.code < 200) {
                            _registerLiveData.postValue(MuskoResource.Error(registerResponse.message))
                        } else {
                            _registerLiveData.postValue(MuskoResource.Success(registerResponse,registerResponse.code.toString()))
                        }
                    } ?: run{
                        _registerLiveData.postValue(MuskoResource.Error("Terjadi Kesalahan"))
                    }
                } else {
                    _registerLiveData.postValue(MuskoResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _registerLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun resetLogin() {
        _loginLiveData.postValue(MuskoResource.Default())
    }

}
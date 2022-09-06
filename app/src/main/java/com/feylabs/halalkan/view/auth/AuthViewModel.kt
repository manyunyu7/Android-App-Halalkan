package com.feylabs.halalkan.view.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.auth.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber
import java.io.File

class AuthViewModel(val repo: QumparanRepository, val ds: RemoteDataSource) : ViewModel() {

    private var _loginLiveData = MutableLiveData<QumparanResource<LoginResponse?>>()
    val loginLiveData get() = _loginLiveData

    private var _userLiveData = MutableLiveData<QumparanResource<MyProfileResponse?>>()
    val userLiveData get() = _userLiveData

    private var _registerLiveData = MutableLiveData<QumparanResource<RegisterResponse?>>()
    val registerLiveData get() = _registerLiveData

    private var _editProfileLiveData = MutableLiveData<QumparanResource<EditProfileResponse?>>()
    val editProfileLiveData get() = _editProfileLiveData

    private var _resetPassLiveData = MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val resetPassLiveData get() = _resetPassLiveData

    private var _userProfileLiveData = MutableLiveData<QumparanResource<UserModel?>>()
    val userProfileLiveData get() = _userProfileLiveData

    fun login(loginBodyRequest: LoginBodyRequest) {
        _loginLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = repo.login(loginBodyRequest)
                Timber.d("users response $")
                if (res.isSuccessful) {

                    val body = res.body()
                    body?.let { loginResponse ->
                        if (loginResponse.mCode > 299 || loginResponse.mCode < 200) {
                            _loginLiveData.postValue(QumparanResource.Error(message = loginResponse.mMessage.orEmpty() + " (Musko)"))
                        } else {
                            _loginLiveData.postValue(
                                QumparanResource.Success(
                                    data = loginResponse,
                                    message = loginResponse.mMessage.toString()
                                )
                            )
                        }
                    }
                } else {
                    _loginLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _loginLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getUserProfile() {
        _userLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getMyProfile()
                Timber.d("users response $")
                if (res.isSuccessful) {

                    val body = res.body()
                    body?.let { loginResponse ->
                        if (loginResponse.code > 299 || loginResponse.code < 200) {
                            _userLiveData.postValue(QumparanResource.Error(message = loginResponse.message.orEmpty() + " (Musko)"))
                        } else {
                            _userLiveData.postValue(
                                QumparanResource.Success(
                                    data = loginResponse,
                                    message = loginResponse.message
                                )
                            )
                        }
                    }
                } else {
                    _userLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _userLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


    fun register(registerBodyRequest: RegisterBodyRequest) {
        _registerLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = repo.register(registerBodyRequest)
                if (res.isSuccessful) {

                    val body = res.body()
                    body?.let { registerResponse ->
                        if (registerResponse.code > 299 || registerResponse.code < 200) {
                            _registerLiveData.postValue(QumparanResource.Error(registerResponse.message))
                        } else {
                            _registerLiveData.postValue(
                                QumparanResource.Success(
                                    registerResponse,
                                    registerResponse.code.toString()
                                )
                            )
                        }
                    } ?: run {
                        _registerLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                    }
                } else {
                    _registerLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _registerLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getUserProfile(id: String) {
        _userProfileLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getUserProfile(id)
                Timber.d("users response $")
                if (res.isSuccessful) {
                    val body = res.body()
                    _userProfileLiveData.postValue(QumparanResource.Success(body))
                } else {
                    _userProfileLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _userProfileLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun resetUserPassword(id: String, new_password: String) {
        _resetPassLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.resetUserPassword(id, new_password)
                Timber.d("users response $")
                if (res.isSuccessful) {
                    val body = res.body()
                    _resetPassLiveData.postValue(QumparanResource.Success(body))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _resetPassLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _resetPassLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun updateProfile(
        name: String,
        phone: String,
        email: String,
        file: File?
    ) {
        viewModelScope.launch {
            _editProfileLiveData.postValue(QumparanResource.Loading())

            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("name", name)
            builder.addFormDataPart("email", email)
            builder.addFormDataPart("phone_number", phone)

            file?.let {
                builder.addFormDataPart(
                    "img",
                    file.name,
                    RequestBody.create(("multipart/form-data").toMediaTypeOrNull(), file)
                )
            }

            val requestBody: MultipartBody = builder.build()

            try {
                val req = ds.updateProfile(requestBody)
                req?.let {
                    if (req.isSuccessful) {
                        _editProfileLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _editProfileLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _editProfileLiveData.postValue(QumparanResource.Error("Unknown Error"))
                }
            } catch (e: Exception) {
                _editProfileLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


    fun resetLogin() {
        _loginLiveData.postValue(QumparanResource.Default())
    }

}
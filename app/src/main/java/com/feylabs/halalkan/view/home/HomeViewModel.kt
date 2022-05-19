package com.feylabs.halalkan.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.MasjidRepository
import com.feylabs.halalkan.data.QumparanRepository
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.PostResponse
import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(val repo: QumparanRepository, val masjidRepo: MasjidRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private var _userLiveData = MutableLiveData<QumparanResource<UserResponse?>>()
    val userLiveData get() = _userLiveData

    private var _postLiveData = MutableLiveData<QumparanResource<PostResponse?>>()
    val postLiveData get() = _postLiveData

    private var _allMasjidLiveData =
        MutableLiveData<QumparanResource<MasjidResponseWithoutPagination?>>()
    val allMasjidLiveData get() = _allMasjidLiveData

    fun fetchAllMasjid() = viewModelScope.launch {
        _allMasjidLiveData.postValue(QumparanResource.Loading())
        try {
            val res = masjidRepo.getAllMasjid()
            if (res.isSuccessful) {
                _allMasjidLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                _allMasjidLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
            }
        } catch (e: Exception) {
            _allMasjidLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }

    fun fetchUsers() = viewModelScope.launch {
        _userLiveData.postValue(QumparanResource.Loading())
        try {
            val res = repo.getUsers()
            Timber.d("users response $")
            if (res.isSuccessful) {
                _userLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                _userLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
            }
        } catch (e: Exception) {
            _userLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }

    fun fetchPosts() = viewModelScope.launch {
        _postLiveData.postValue(QumparanResource.Loading())
        try {
            val res = repo.getPosts()
            Timber.d("users response $")
            if (res.isSuccessful) {
                _postLiveData.postValue(QumparanResource.Success(res.body()))
            } else {
                _postLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
            }
        } catch (e: Exception) {
            _postLiveData.postValue(QumparanResource.Error(e.message.toString()))
        }
    }

}
package com.feylabs.halalkan.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.MasjidRepository
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.PostResponse
import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    val repo: QumparanRepository,
    val masjidRepo: MasjidRepository,
    val ds : RemoteDataSource
    ) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private var _userLiveData = MutableLiveData<MuskoResource<UserResponse?>>()
    val userLiveData get() = _userLiveData

    private var _postLiveData = MutableLiveData<MuskoResource<PostResponse?>>()
    val postLiveData get() = _postLiveData

    private var _allMasjidLiveData =
        MutableLiveData<MuskoResource<MasjidResponseWithoutPagination?>>()
    val allMasjidLiveData get() = _allMasjidLiveData


    private var _prayerTimeSingleLiveData:
            MutableLiveData<MuskoResource<PrayerTimeAladhanSingleDateResponse?>> =
        MutableLiveData()
    val prayerTimeSingleLiveData get() = _prayerTimeSingleLiveData

    fun fetchPrayerTimeSingle(
        latitude: Double,
        longitude: Double,
        method: String,
        time: String
    ) {
        viewModelScope.launch {
            try {
                val res = ds.getPrayerTimeSingleDate(
                    lat = latitude.toString(),
                    long = longitude.toString(),
                    method = method,
                    time = time
                )
                if (res.isSuccessful) {
                    _prayerTimeSingleLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _prayerTimeSingleLiveData.postValue(MuskoResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _prayerTimeSingleLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun fetchAllMasjid() = viewModelScope.launch {
        _allMasjidLiveData.postValue(MuskoResource.Loading())
        try {
            val res = masjidRepo.getAllMasjid()
            if (res.isSuccessful) {
                _allMasjidLiveData.postValue(MuskoResource.Success(res.body()))
            } else {
                _allMasjidLiveData.postValue(MuskoResource.Error("Terjadi Kesalahan"))
            }
        } catch (e: Exception) {
            _allMasjidLiveData.postValue(MuskoResource.Error(e.message.toString()))
        }
    }

    fun fetchUsers() = viewModelScope.launch {
        _userLiveData.postValue(MuskoResource.Loading())
        try {
            val res = repo.getUsers()
            Timber.d("users response $")
            if (res.isSuccessful) {
                _userLiveData.postValue(MuskoResource.Success(res.body()))
            } else {
                _userLiveData.postValue(MuskoResource.Error("Terjadi Kesalahan"))
            }
        } catch (e: Exception) {
            _userLiveData.postValue(MuskoResource.Error(e.message.toString()))
        }
    }

    fun fetchPosts() = viewModelScope.launch {
        _postLiveData.postValue(MuskoResource.Loading())
        try {
            val res = repo.getPosts()
            Timber.d("users response $")
            if (res.isSuccessful) {
                _postLiveData.postValue(MuskoResource.Success(res.body()))
            } else {
                _postLiveData.postValue(MuskoResource.Error("Terjadi Kesalahan"))
            }
        } catch (e: Exception) {
            _postLiveData.postValue(MuskoResource.Error(e.message.toString()))
        }
    }

}
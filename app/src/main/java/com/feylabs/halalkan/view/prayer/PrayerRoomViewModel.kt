package com.feylabs.halalkan.view.prayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.MasjidRepository
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidDetailResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import kotlinx.coroutines.launch

class PrayerRoomViewModel(
    val repo: QumparanRepository,
    val masjidRepository: MasjidRepository
) : ViewModel() {

    private var _masjidLiveData =
        MutableLiveData<QumparanResource<MasjidResponseWithoutPagination?>>()
    val masjidLiveData get() = _masjidLiveData


    private var _masjidDetailLiveData =
        MutableLiveData<QumparanResource<MasjidDetailResponse?>>()
    val masjidDetailLiveData get() = _masjidDetailLiveData


    private var _masjidPhotoLiveData =
        MutableLiveData<QumparanResource<MasjidPhotosResponse?>>()
    val masjidPhotoLiveData get() = _masjidPhotoLiveData

    fun getMasjidPhoto(id:String){
        viewModelScope.launch {
            try {
                val res = masjidRepository.getMasjidPhotos(id)
                if (res.isSuccessful) {
                    _masjidPhotoLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidPhotoLiveData.postValue(QumparanResource.Error(res.errorBody().toString()))
                }
            } catch (e: Exception) {
                _masjidLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getDetailMasjid(id:String){
        viewModelScope.launch {
            _masjidDetailLiveData.postValue(QumparanResource.Loading())
            try {
                val res = masjidRepository.getMasjidDetail(id)
                if (res.isSuccessful) {
                    _masjidDetailLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidDetailLiveData.postValue(QumparanResource.Error(res.errorBody().toString()))
                }
            } catch (e: Exception) {
                _masjidDetailLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


    fun getAllMosque() {
        viewModelScope.launch {
            try {
                val res = repo.getMasjids()
                if (res.isSuccessful) {
                    _masjidLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _masjidLiveData.postValue(QumparanResource.Error(res.errorBody().toString()))
                }
            } catch (e: Exception) {
                _masjidLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

}
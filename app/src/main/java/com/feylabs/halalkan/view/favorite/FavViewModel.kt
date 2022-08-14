package com.feylabs.halalkan.view.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavMasjidResponse
import com.feylabs.halalkan.data.remote.reqres.favorite.AddFavRestoResponse
import com.feylabs.halalkan.data.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavViewModel(
    val ds: FavoriteRepository
) : ViewModel() {

    private var _addMasjidLiveData =
        MutableLiveData<QumparanResource<AddFavMasjidResponse?>>()
    val addMasjidLiveData get() = _addMasjidLiveData


    private var _addRestoLiveData =
        MutableLiveData<QumparanResource<AddFavRestoResponse?>>()
    val addRestoLiveData get() = _addRestoLiveData


    fun addFavMasjid(masjidId: String) {
        _addMasjidLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.addFavoriteMasjid(masjidId)
                if (res.isSuccessful) {
                    _addMasjidLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _addMasjidLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _addMasjidLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun addFavResto(restoId: String) {
        _addRestoLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.addFavoriteResto(restoId)
                if (res.isSuccessful) {
                    _addRestoLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _addRestoLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _addMasjidLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


}
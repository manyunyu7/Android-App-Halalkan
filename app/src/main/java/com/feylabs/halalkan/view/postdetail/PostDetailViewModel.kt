package com.feylabs.halalkan.view.postdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.AlbumPhotoResponse
import com.feylabs.halalkan.data.remote.reqres.PostCommentResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class PostDetailViewModel(val repo: QumparanRepository) : ViewModel() {

    private var _commentLiveData = MutableLiveData<QumparanResource<PostCommentResponse?>>()
    val commentLiveData get() = _commentLiveData

    private var _photosLiveData = MutableLiveData<QumparanResource<AlbumPhotoResponse?>>()
    val photosLiveData get() = _photosLiveData

    fun getPostComment(postId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getPostComment(postId)
                Timber.d("users response $")
                if (res.isSuccessful) {
                    _commentLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _commentLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _commentLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getPhotosByAlbum(albumId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getAlbumPhoto(albumId)
                Timber.d("users response $")
                if (res.isSuccessful) {
                    _photosLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _photosLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _photosLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }
}
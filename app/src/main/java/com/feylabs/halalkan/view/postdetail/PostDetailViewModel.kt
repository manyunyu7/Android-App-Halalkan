package com.feylabs.halalkan.view.postdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.reqres.AlbumPhotoResponse
import com.feylabs.halalkan.data.remote.reqres.PostCommentResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class PostDetailViewModel(val repo: QumparanRepository) : ViewModel() {

    private var _commentLiveData = MutableLiveData<MuskoResource<PostCommentResponse?>>()
    val commentLiveData get() = _commentLiveData

    private var _photosLiveData = MutableLiveData<MuskoResource<AlbumPhotoResponse?>>()
    val photosLiveData get() = _photosLiveData

    fun getPostComment(postId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getPostComment(postId)
                Timber.d("users response $")
                if (res.isSuccessful) {
                    _commentLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _commentLiveData.postValue(MuskoResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _commentLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }

    fun getPhotosByAlbum(albumId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getAlbumPhoto(albumId)
                Timber.d("users response $")
                if (res.isSuccessful) {
                    _photosLiveData.postValue(MuskoResource.Success(res.body()))
                } else {
                    _photosLiveData.postValue(MuskoResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _photosLiveData.postValue(MuskoResource.Error(e.message.toString()))
            }
        }
    }
}
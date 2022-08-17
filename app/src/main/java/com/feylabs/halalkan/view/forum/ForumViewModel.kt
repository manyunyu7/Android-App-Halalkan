package com.feylabs.halalkan.view.forum

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.forum.AllForumPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.forum.CreateForumResponse
import com.feylabs.halalkan.data.remote.reqres.forum.ForumCategoryResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File

class ForumViewModel(
    val ds: RemoteDataSource
) : ViewModel() {

    private var _forumCategoryLiveData =
        MutableLiveData<QumparanResource<ForumCategoryResponse?>>()
    val forumCategoryLiveData get() = _forumCategoryLiveData

    private var _allForumLiveData =
        MutableLiveData<QumparanResource<AllForumPaginationResponse?>>()
    val allForumLiveData get() = _allForumLiveData

    private var _createForumLiveData =
        MutableLiveData<QumparanResource<CreateForumResponse?>>()
    val createForumLiveData get() = _createForumLiveData

    fun createThread(
        title: String,
        desc: String,
        category: String,
        file: File?
    ) {
        viewModelScope.launch {
            _createForumLiveData.postValue(QumparanResource.Loading())
            val builder = MultipartBody.Builder()
            builder.setType(MultipartBody.FORM)
            builder.addFormDataPart("category_id", category)
            builder.addFormDataPart("title", title)
            builder.addFormDataPart("body", desc)

            file?.let {
                builder.addFormDataPart(
                    "img",
                    file.name,
                    RequestBody.create(("multipart/form-data").toMediaTypeOrNull(), file)
                )
            }

            val requestBody: MultipartBody = builder.build()

            try {
                val req = ds.createForum(requestBody)
                req?.let {
                    if (req.isSuccessful) {
                        _createForumLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _createForumLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _createForumLiveData.postValue(QumparanResource.Error("Unknown Error"))
                }
            } catch (e: Exception) {
                _createForumLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getForumCategory() {
        _forumCategoryLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getForumCategory()
                if (res.isSuccessful) {
                    _forumCategoryLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _forumCategoryLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _forumCategoryLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getForumPagination(page: Int,perPage:Int=10) {
        _allForumLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getAllForumPaginate(page = page,perPage)
                if (res.isSuccessful) {
                    _allForumLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _allForumLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _allForumLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


}
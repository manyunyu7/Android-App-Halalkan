package com.feylabs.halalkan.view.forum

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.GeneralApiResponse
import com.feylabs.halalkan.data.remote.reqres.forum.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.create
import org.json.JSONObject
import java.io.File


class ForumViewModel(
    val ds: RemoteDataSource
) : ViewModel() {

    private var _forumCategoryLiveData =
        MutableLiveData<QumparanResource<ForumCategoryResponse?>>()
    val forumCategoryLiveData get() = _forumCategoryLiveData

    private var _deleteForumLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val deleteForumLiveData get() = _deleteForumLiveData

    private var _deleteCommentLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val deleteCommentLiveData get() = _deleteCommentLiveData

    private var _detailForumLiveData =
        MutableLiveData<QumparanResource<ForumDetailResponse?>>()
    val detailForumLiveData get() = _detailForumLiveData

    private var _commentOnForumLiveData =
        MutableLiveData<QumparanResource<ForumCommentResponse?>>()
    val commentOnForumLiveData get() = _commentOnForumLiveData

    private var _allForumLiveData =
        MutableLiveData<QumparanResource<AllForumPaginationResponse?>>()
    val allForumLiveData get() = _allForumLiveData

    private var _likeLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val likeLiveData get() = _likeLiveData

    private var _unlikeLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val unlikeLiveData get() = _unlikeLiveData

    private var _createCommentLiveData =
        MutableLiveData<QumparanResource<AddCommentResponse?>>()
    val createCommentLiveData get() = _createCommentLiveData

    private var _likeCommentLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val likeCommentLiveData get() = _likeCommentLiveData

    private var _unlikeCommentLiveData =
        MutableLiveData<QumparanResource<GeneralApiResponse?>>()
    val unlikeCommentLiveData get() = _unlikeCommentLiveData

    private var _createForumLiveData =
        MutableLiveData<QumparanResource<CreateForumResponse?>>()
    val createForumLiveData get() = _createForumLiveData

    private var _updateForumLiveData =
        MutableLiveData<QumparanResource<CreateForumResponse?>>()
    val updateForumLiveData get() = _updateForumLiveData

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
            builder.addFormDataPart("img", "")

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


    fun updateThread(
        id: String,
        title: String,
        desc: String,
        category: String,
        isDeletingImage: Boolean,
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
                val req = ds.updateForum(id, requestBody, isDeletingImage = isDeletingImage)
                req?.let {
                    if (req.isSuccessful) {
                        _updateForumLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _updateForumLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _updateForumLiveData.postValue(QumparanResource.Error("Unknown Error"))
                }
            } catch (e: Exception) {
                _updateForumLiveData.postValue(QumparanResource.Error(e.message.toString()))
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

    fun getForumDetail(id: Int) {
        _detailForumLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getDetailForum(id)
                if (res.isSuccessful) {
                    _detailForumLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _detailForumLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _detailForumLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun deleteForum(id: Int) {
        _deleteForumLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.deleteForum(id)
                if (res.isSuccessful) {
                    _deleteForumLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _deleteForumLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _deleteForumLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun deleteComment(id: Int) {
        _deleteCommentLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.deleteComment(id)
                if (res.isSuccessful) {
                    _deleteCommentLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _deleteCommentLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _deleteCommentLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getCommentOnForum(id: Int) {
        _commentOnForumLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getCommentForum(id)
                if (res.isSuccessful) {
                    _commentOnForumLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    var message = res.message().toString()
                    res.errorBody()?.let {
                        val jsonObj = JSONObject(it.charStream().readText())
                        message = jsonObj.getString("message")
                    }
                    _commentOnForumLiveData.postValue(QumparanResource.Error(message))
                }
            } catch (e: Exception) {
                _commentOnForumLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getForumPagination(page: Int, perPage: Int = 10) {
        _allForumLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.getAllForumPaginate(page = page, perPage)
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

    fun likeForum(forumId: Int) {
        _likeLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.likeForum(forumId)
                if (res.isSuccessful) {
                    _likeLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _likeLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _likeLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun unlikeForum(forumId: Int) {
        _unlikeLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.unlikeForum(forumId)
                if (res.isSuccessful) {
                    _unlikeLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _unlikeLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _unlikeLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun likeComment(forumId: Int) {
        _likeCommentLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.likeComment(forumId)
                if (res.isSuccessful) {
                    _likeCommentLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _likeCommentLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _likeCommentLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun unlikeComment(forumId: Int) {
        _unlikeCommentLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.unlikeComment(forumId)
                if (res.isSuccessful) {
                    _unlikeCommentLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _unlikeCommentLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _unlikeCommentLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun addComment(body: CreateCommentPayload) {
        _createCommentLiveData.postValue(QumparanResource.Loading())
        viewModelScope.launch {
            try {
                val res = ds.createComment(body)
                if (res.isSuccessful) {
                    _createCommentLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _createCommentLiveData.postValue(
                        QumparanResource.Error(
                            res.errorBody().toString()
                        )
                    )
                }
            } catch (e: Exception) {
                _createCommentLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun fireAllForumLiveData() {
        _allForumLiveData.postValue(QumparanResource.Default())
    }


}
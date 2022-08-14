package com.feylabs.halalkan.view.prayer.review

import android.text.Editable
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Companion.FORM
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.create
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File


class PrayerRoomReviewViewModel(
    val ds: RemoteDataSource
) : ViewModel() {

    private var _masjidLiveData =
        MutableLiveData<QumparanResource<MasjidResponseWithoutPagination?>>()
    val masjidLiveData get() = _masjidLiveData

    private var _createReviewLiveData =
        MutableLiveData<QumparanResource<ResponseBody?>>()
    val createReviewLiveData get() = _createReviewLiveData

    fun addReview(
        ratingId: String,
        masjidId: String,
        comment: Editable? = null,
        filePaths: MutableList<File>
    ) {
        viewModelScope.launch {
            _createReviewLiveData.postValue(QumparanResource.Loading())
            val builder = MultipartBody.Builder()
            builder.setType(FORM)
            builder.addFormDataPart("rating_id", ratingId)
            if (comment.isNullOrEmpty().not())
                builder.addFormDataPart("comment", comment.toString())
            else builder.addFormDataPart("comment","-")

            filePaths.forEachIndexed { index, file ->
                builder.addFormDataPart(
                    "img[]",
                    file.name,
                    create(("multipart/form-data").toMediaTypeOrNull(), file)
                )
            }
            val requestBody: MultipartBody = builder.build()

            try {
                val req = ds.createMasjidReview(masjidId, requestBody)
                req?.let {
                    if (req.isSuccessful) {
                        _createReviewLiveData.postValue(QumparanResource.Success(req.body()))
                    } else {
                        var message = req.message().toString()
                        req.errorBody()?.let {
                            val jsonObj = JSONObject(it.charStream().readText())
                            message = jsonObj.getString("message")
                        }
                        _createReviewLiveData.postValue(QumparanResource.Error(message))
                    }
                } ?: run {
                    _createReviewLiveData.postValue(QumparanResource.Error("Null"))
                }
            } catch (e: Exception) {
                _createReviewLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }


}
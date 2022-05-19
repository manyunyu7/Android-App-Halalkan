package com.feylabs.halalkan.view.translate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.TranslatorRepository
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.translator.TiktokTextToSpeechResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TranslateResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class TranslateViewModel(val repo: TranslatorRepository) : ViewModel() {

    private var _translateLiveData = MutableLiveData<QumparanResource<TranslateResponse?>>()
    val translateLiveData get() = _translateLiveData

    val sourceLanguage = MutableLiveData("id")
    val targetLanguage = MutableLiveData("ko")

    val sourceLanguageDesc = MutableLiveData("Indonesia")
    val targetLanguageDesc = MutableLiveData("Korea")

    private var _ttlLiveData = MutableLiveData<QumparanResource<TiktokTextToSpeechResponse?>>()
    val ttlLiveData get() = _ttlLiveData


    val clickedContainer = MutableLiveData(0)

    fun getTextToSpeech(text: String, isSource: Boolean = false) {
        viewModelScope.launch {
            try {
                val source = sourceLanguage.value ?: "id"
                val target = targetLanguage.value ?: "ko"

                var mSource = ""
                mSource = if (isSource)
                    source
                else target
                val res = repo.getTextToSpeech(mSource, text)
                Timber.d("users response $")
                if (res.isSuccessful) {
                    _ttlLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _ttlLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _ttlLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

    fun getTranslation(text: String) {
        viewModelScope.launch {
            try {
                val source = sourceLanguage.value ?: "id"
                val target = targetLanguage.value ?: "ko"
                val res = repo.getTranslation(source, target, text)
                Timber.d("users response $")
                if (res.isSuccessful) {
                    _translateLiveData.postValue(QumparanResource.Success(res.body()))
                } else {
                    _translateLiveData.postValue(QumparanResource.Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _translateLiveData.postValue(QumparanResource.Error(e.message.toString()))
            }
        }
    }

}
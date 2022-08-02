package com.feylabs.halalkan.view.translate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.repository.TranslatorRepository
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.MuskoResource.*
import com.feylabs.halalkan.data.remote.reqres.translator.TiktokTextToSpeechResponse
import com.feylabs.halalkan.data.remote.reqres.translator.TranslateResponse
import com.feylabs.halalkan.databinding.ItemChatBinding
import com.feylabs.halalkan.view.translate.convo.ConvoModel
import kotlinx.coroutines.launch
import timber.log.Timber

class TranslateViewModel(val repo: TranslatorRepository) : ViewModel() {

    private var _translateLiveData = MutableLiveData<MuskoResource<TranslateResponse?>>()
    val translateLiveData get() = _translateLiveData

    private var _convoTranslateLiveData =
        MutableLiveData<MuskoResource<Pair<ItemChatBinding, TranslateResponse?>>>()
    val convoTranslateLiveData get() = _convoTranslateLiveData

    // first : source, second : target
    val languagePair = MutableLiveData<Pair<String, String>>()

    val sourceLanguage = MutableLiveData("id")
    val targetLanguage = MutableLiveData("ko")

    val sourceLanguageDesc = MutableLiveData("Indonesia")
    val targetLanguageDesc = MutableLiveData("Korea")

    private var _ttlLiveData = MutableLiveData<MuskoResource<TiktokTextToSpeechResponse?>>()
    val ttlLiveData get() = _ttlLiveData


    val clickedContainer = MutableLiveData(0)
    val clickedMicrophone = MutableLiveData(0)
    val textSpeaked = MutableLiveData("")

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
                    _ttlLiveData.postValue(Success(res.body()))
                } else {
                    _ttlLiveData.postValue(Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _ttlLiveData.postValue(Error(e.message.toString()))
            }
        }
    }

    fun getTextToSpeechConvo(convoModel: ConvoModel) {
        viewModelScope.launch {
            try {
                var mFrom = convoModel.from
                if(convoModel.type=="1"){
                    mFrom=convoModel.to
                }
                val res = repo.getTextToSpeech(mFrom, convoModel.result)
                if (res.isSuccessful) {
                    _ttlLiveData.postValue(Success(res.body()))
                } else {
                    _ttlLiveData.postValue(Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _ttlLiveData.postValue(Error(e.message.toString()))
            }
        }
    }

    fun getTranslation(text: String, type: String? = null) {
        viewModelScope.launch {
            try {
                val source = sourceLanguage.value ?: "id"
                val target = targetLanguage.value ?: "ko"

                var mSource = source
                var mTarget = target

                type?.let {
                    if (it == "target" && it.isNotEmpty()) {
                        mSource = target
                        mTarget = source
                    }
                }

                val res = repo.getTranslation(mSource, mTarget, text)

                if (res.isSuccessful) {
                    _translateLiveData.postValue(Success(res.body()))
                } else {
                    _translateLiveData.postValue(Error("Terjadi Kesalahan"))
                }
            } catch (e: Exception) {
                _translateLiveData.postValue(Error(e.message.toString()))
            }
        }
    }


}
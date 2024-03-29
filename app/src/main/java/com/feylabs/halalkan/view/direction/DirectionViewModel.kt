package com.feylabs.halalkan.view.direction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.halalkan.data.repository.QumparanRepository
import com.feylabs.halalkan.data.remote.QumparanResource
import org.json.JSONArray
import org.json.JSONObject

class DirectionViewModel(val repo: QumparanRepository) : ViewModel() {

    private var _directionLiveData = MutableLiveData<QumparanResource<JSONArray>>()
    val directionLiveData get() = _directionLiveData

    val url = "https://maps.googleapis.com/maps/api/directions/json"

    fun getDirection(origin: String,destination:String,key:String) {
        AndroidNetworking.get(url)
            .addQueryParameter("destination",destination)
            .addQueryParameter("origin",origin)
            .addQueryParameter("mode","driving")
            .addQueryParameter("key",key)
            .build()
            .getAsJSONObject(object :JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
                    try {
                        val status = response.getString("status")
                        if(status=="OK"){
                            val routes = response.getJSONArray("routes")
                            _directionLiveData.postValue(QumparanResource.Success(routes))
                        }else{
                            _directionLiveData.postValue(QumparanResource.Error(response.toString()))
                        }
                    }catch (e:Exception){
                        _directionLiveData.postValue(QumparanResource.Error(message = e.message.toString()))
                    }

                }

                override fun onError(anError: ANError?) {
                    _directionLiveData.postValue(QumparanResource.Error(message = anError.toString()))
                }
            })
    }

}
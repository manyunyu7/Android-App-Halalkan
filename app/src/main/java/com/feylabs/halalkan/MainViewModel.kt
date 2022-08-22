package com.feylabs.halalkan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.utils.location.MyLatLong
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : ViewModel() {

    val liveLatLng = MutableLiveData(MyLatLong(-99.0, -99.0))
    val liveLatitude = MutableLiveData(-99.0)
    val liveLongitude = MutableLiveData(-99.0)
    val liveAddress = MutableLiveData("-")
    val liveKecamatan = MutableLiveData("-")

    fun getLat(): Double {
        return this.liveLatitude.value ?: 37.603240974765676
    }

    fun getLatLong(): LatLng {
        return LatLng(getLat(), getLong())
    }

    fun getAddress() : String{
        return liveAddress.value.orEmpty()
    }

    fun getLong(): Double {
        return this.liveLongitude.value ?: 126.87388681299839
    }

}
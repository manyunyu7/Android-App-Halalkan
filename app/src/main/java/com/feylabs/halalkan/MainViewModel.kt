package com.feylabs.halalkan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.feylabs.halalkan.utils.location.MyLatLong

class MainViewModel : ViewModel() {

    val liveLatLng = MutableLiveData(MyLatLong(-99.0,-99.0))
    val liveLatitude = MutableLiveData(-99.0)
    val liveLongitude = MutableLiveData(-99.0)
    val liveAddress = MutableLiveData("-")
    val liveKecamatan = MutableLiveData("-")

}
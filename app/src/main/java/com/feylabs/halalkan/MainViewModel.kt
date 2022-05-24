package com.feylabs.halalkan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val liveLatitude = MutableLiveData(-99.0)
    val liveLongitude = MutableLiveData(-99.0)
    val liveAddress = MutableLiveData("-")
}
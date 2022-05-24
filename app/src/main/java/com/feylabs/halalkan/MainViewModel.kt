package com.feylabs.halalkan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : ViewModel() {

    val liveLatLng = MutableLiveData(Pair(-99.0,-99.0))
    val liveLatitude = MutableLiveData(-99.0)
    val liveLongitude = MutableLiveData(-99.0)
    val liveAddress = MutableLiveData("-")
    val liveKecamatan = MutableLiveData("-")

}
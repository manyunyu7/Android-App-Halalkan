package com.feylabs.halalkan.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.ActivityContainerBinding
import com.feylabs.halalkan.utils.PermissionActivityFlow
import com.feylabs.halalkan.utils.PermissionUtil
import com.feylabs.halalkan.utils.PermissionUtil.Companion.isGranted
import com.feylabs.halalkan.utils.base.BaseActivity
import com.feylabs.halalkan.utils.location.MyLatLong
import com.google.android.gms.location.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*


class ContainerActivity : BaseActivity(), LocationListener {

    private lateinit var binding: ActivityContainerBinding

    // declare a global variable of FusedLocationProviderClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // globally declare LocationRequest
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback


    val mainViewModel: MainViewModel by viewModel<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAction()
        initObserver()

        // in onCreate() initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationUpdates()


        // val navView: BottomNavigationView = binding.navView
        // window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val navController = findNavController(R.id.nav_host_fragment_activity_container)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_newHomeFragment,
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)
    }

    private fun initAction() {
        binding.btnClose.setOnClickListener {
            binding.bottomContainer.makeGone()
        }

        binding.btnDebug.setOnClickListener {
            binding.bottomContainer.makeVisible()
            binding.bottomContainer.animation = AnimationUtils.loadAnimation(
                this,R.anim.nav_default_enter_anim
            )
        }
    }

    private fun initObserver() {
        mainViewModel.liveLatLng.observe(this) {
            binding.tvLong.text = it.lat.toString()
            binding.tvLat.text = it.long.toString()
        }
    }


    /**
     * call this method for receive location
     * get location and give callback when successfully retrieve
     * function itself check location permission before access related methods
     */
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        if (checkLocationPemission()) {
            showToast("Mengambil Data Lokasi Terbaru")
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        // use your location object
                        // get latitude , longitude and other info from this
                        extractGeoCoder(location)
                    }
                }
        }
    }

    private fun extractGeoCoder(loc: Location) {
        /*------- To get city name from coordinates -------- */
        val gcd = Geocoder(baseContext, Locale.getDefault())
        val addresses: List<Address>
        mainViewModel.liveLatLng.postValue(MyLatLong(loc.latitude,loc.longitude))
        try {
            addresses = gcd.getFromLocation(
                loc.latitude,
                loc.longitude, 1
            )
            if (addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)
                val kecamatan = addresses[0].locality ?: ""
                val state = addresses[0].adminArea ?: ""
                val country = addresses[0].countryName ?: ""
                val postalCode = addresses[0].postalCode ?: ""
                mainViewModel.liveKecamatan.postValue(kecamatan)
                mainViewModel.liveAddress.postValue("$address - $postalCode - $kecamatan - $state, $country")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /* Check is there a NetworkConnection */
    protected fun internetIsConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty())
            when (requestCode) {
                PermissionActivityFlow.TRANSLATE_MIC.code -> {

                }
                PermissionActivityFlow.LOCATION_INIT.code -> {

                }
            }
    }

    override fun onLocationChanged(p0: Location) {
        getLastKnownLocation()
    }

    /**
     * call this method in onCreate
     * onLocationResult call when location is changed
     */
    private fun getLocationUpdates() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationRequest.interval = 50000
        locationRequest.fastestInterval = 50000
        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location = locationResult.lastLocation
                    extractGeoCoder(location)
                    // use your location object
                    // get latitude , longitude and other info from this
                }
            }
        }
    }


    //start location updates
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (checkLocationPemission()) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null /* Looper */
            )
        }
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    // start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun checkLocationPemission(): Boolean {
        val permLocCoarse = PermissionUtil.getPermissionStatus(
            this,
            PermissionUtil.PER_LOCATION_COARSE
        )
        val permLocFine =
            PermissionUtil.getPermissionStatus(this, PermissionUtil.PER_LOCATION_FINE)

        return permLocCoarse.isGranted() && permLocFine.isGranted()
    }
}
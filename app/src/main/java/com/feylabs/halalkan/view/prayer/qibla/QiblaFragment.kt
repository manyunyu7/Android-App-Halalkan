package com.feylabs.halalkan.view.prayer.qibla

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.app.ActivityCompat
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.databinding.FragmentQiblaBinding
import com.feylabs.halalkan.utils.Compass
import com.feylabs.halalkan.utils.GPSTracker
import com.feylabs.halalkan.utils.base.BaseFragment


class QiblaFragment : BaseFragment() {

    private var _binding: FragmentQiblaBinding? = null
    private val binding get() = _binding as FragmentQiblaBinding
    var gps: GPSTracker? = null
    private var currentAzimuth = 0f
    private val TAG = "QiblaFinder"
    private var compass: Compass? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQiblaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initUI() {
        setupCompass()
    }

    override fun initObserver() {
    }

    override fun initAction() {
    }

    override fun initData() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gps = GPSTracker(requireContext())
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun fetch_GPS() {
        var result = 0.0
        gps = GPSTracker(requireContext())
        if (gps!!.canGetLocation()) {
            val latitude = gps!!.latitude
            val longitude = gps!!.longitude
            binding.textDown.setText(
                """
                Kordinat lat $latitude and long$longitude
                """.trimIndent()
            )
            Log.e("TAG", "GPS is on")
            val lat_saya = gps!!.latitude
            val lon_saya = gps!!.longitude
            if (lat_saya < 0.001 && lon_saya < 0.001) {
                binding.mainImageQibla.setVisibility(View.INVISIBLE)
                binding.mainImageQibla.setVisibility(View.GONE)
//                text_up.setText("")
//                text_down.setText(resources.getString(R.string.locationunready))
            } else {
                val longitude2 =
                    39.826209 // Kaabah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
                val latitude2 =
                    Math.toRadians(21.422507) // Kaabah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
                val latitude1 = Math.toRadians(lat_saya)
                val longDiff = Math.toRadians(longitude2 - lon_saya)
                val y = Math.sin(longDiff) * Math.cos(latitude2)
                val x = Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(
                    latitude2
                ) * Math.cos(longDiff)
                result = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360
                val result2 = result.toFloat()
                MyPreference(requireContext()).saveFloat("QiblaDegree", result2)
                binding.textUp.setText(
                    "Arah Kiblat " + " " + result2 + " " + "Dari Utara"
                )
                binding.mainImageQibla.setVisibility(View.VISIBLE)
            }
        } else {
            gps!!.showSettingsAlert()
            binding.mainImageQibla.setVisibility(View.INVISIBLE)
            binding.mainImageQibla.setVisibility(View.GONE)
            binding.textUp.setText("")
            binding.textDown.setText("GPS Tidak Aktif")
        }
    }


    private fun setupCompass() {
        val permission_granted: Boolean =
            MyPreference(requireContext()).getPrefBool("permission_granted")
        if (permission_granted) {
            getBearing()
        } else {
            binding.textUp.setText("")
            binding.textDown.setText("Permission GPS Not Granted")
            getBearing()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
            } else {
                getBearing()
            }
        }
        compass = Compass(requireContext())
        val cl: Compass.CompassListener = object : Compass.CompassListener {
            override fun onNewAzimuth(azimuth: Float) {
                adjustGambarDial(azimuth)
                adjustArrowQiblat(azimuth)
            }
        }
        compass!!.setListener(cl)
    }

    private fun getBearing() {
        fetch_GPS()
    }


    fun adjustGambarDial(azimuth: Float) {
        val an: Animation = RotateAnimation(
            -currentAzimuth,
            -azimuth,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuth = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
        binding.mainImageDial.startAnimation(an)
    }

    fun adjustArrowQiblat(azimuth: Float) {
        val QiblaDegree: Float = MyPreference(requireContext()).getPrefFloat("QiblaDegree")
        val an: Animation = RotateAnimation(
            -currentAzimuth + QiblaDegree,
            -azimuth,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        currentAzimuth = azimuth
        an.duration = 500
        an.repeatCount = 0
        an.fillAfter = true
        binding.mainImageQibla.startAnimation(an)
        if (QiblaDegree > 0) {
            binding.mainImageQibla.setVisibility(View.VISIBLE)
        } else {
            binding.mainImageQibla.setVisibility(View.INVISIBLE)
            binding.mainImageQibla.setVisibility(View.GONE)
        }
    }

    override fun onStart() {
        super.onStart()
        if (compass != null) {
            compass!!.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (compass != null) {
            compass!!.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (compass != null) {
            compass!!.start()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted, yay! Do the
                    MyPreference(requireContext()).save("permission_granted", true)
                    getBearing()
                    //text_down.setText(getResources().getString(R.string.permissiongaranted));
                    //arrowViewQiblat.setVisibility(INVISIBLE);
                    //arrowViewQiblat.setVisibility(View.GONE);
                } else {
                }
                return
            }
        }

    }


}
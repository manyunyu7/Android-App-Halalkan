package com.feylabs.halalkan.view.admin_resto.signature

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.adevinta.leku.*
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.customview.imagepreviewcontainer.TypePhotoModel
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.data.remote.reqres.resto.SaveRestoPayload
import com.feylabs.halalkan.databinding.FragmentAddEditRestoBinding
import com.feylabs.halalkan.databinding.FragmentXrestoCurrentBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImage
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.admin_resto.AdminRestoViewModel
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File


class CurrentRestaurantFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoCurrentBinding? = null
    private val binding get() = _binding!!

    val mainViewModel: MainViewModel by sharedViewModel()
    val viewModel by viewModel<AdminRestoViewModel>()

    private val PERMISSION_CODE_STORAGE = 1001

    private var mapCert = mutableMapOf<String, String>()
    private var mapFoodType = mutableMapOf<String, String>()

    var coverPhoto: File? = null


    override fun initUI() {
        arguments?.getString("name")?.let {
            binding.pageTitle.text = it
        }
    }

    override fun initObserver() {
        viewModel.getDetailResto(getRestoId())
        viewModel.myRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {}
                is Success -> {
                    setupRestoData(it.data)
                }
            }
        }


    }

    private fun setupUiFromNetwork(it: RestoDetailResponse) {

    }

    private fun getScreenType(): String {
        val type = arguments?.getString("type") ?: ""
        return type
    }

    private fun getRestoId(): String {
        val type = arguments?.getString("id") ?: ""
        return type
    }

    override fun initAction() {
        binding.menuCategory.setOnClickListener {
            findNavController().navigate(R.id.navigation_menuCategoryFragment)
        }

        binding.menuFood.setOnClickListener {
            findNavController().navigate(R.id.navigation_manageFoodFragment)
        }

        binding.labelInfoResto.setOnClickListener {
            val lat = mainViewModel.liveLatitude.value ?: -99
            val long = mainViewModel.liveLongitude.value ?: -99

            val locationPickerIntent = LocationPickerActivity.Builder()
                .withLocation(lat.toDouble(), long.toDouble())
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withStreetHidden()
                .withCityHidden()
                .withZipCodeHidden()
                .withSatelliteViewHidden()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(requireContext())

            startActivityForResult(locationPickerIntent, 212)
        }

        setupMenuAction()
    }

    private fun setupMenuAction() {
        binding.apply {
            menuAddress.build("Address", getMuskoDrawable(R.drawable.ic_menu_resto_address))
            menuRestoPhone.build("Phone", getMuskoDrawable(R.drawable.ic_menu_resto_phone))
            menuOperatingHour.build(
                "Operating Hour",
                getMuskoDrawable(R.drawable.ic_menu_resto_operating_hours)
            )
            menuTypeResto.build(
                "Type of Restaurant",
                getMuskoDrawable(R.drawable.ic_menu_resto_type)
            )
            menuCertification.build(
                "Certification",
                getMuskoDrawable(R.drawable.ic_menu_resto_certified)
            )

            menuCertification.setOnClickListener {
                findNavController().navigate(R.id.navigation_editRestoCertificationFragment)
            }

            menuTypeResto.setOnClickListener {
                findNavController().navigate(R.id.navigation_editRestoTypeFragment)
            }

            menuRestoPhone.setOnClickListener {
                findNavController().navigate(R.id.navigation_editRestoPhoneFragment)
            }
        }
    }

    override fun initData() {
        viewModel.getRestoCert()
        viewModel.getFoodType()
    }


    private fun pickPhoto() {
        val config = GalleryConfig.Build()
            .limitPickPhoto(5)
            .singlePhoto(false)
            .build()

        RazkyGalleryActivity.openActivityFromFragment(this, 120, config)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoCurrentBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRestoData(data: AllRestoNoPagination?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 212) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                val address = data.getStringExtra(LOCATION_ADDRESS)
                val lekuPoi = data.getParcelableExtra<LekuPoi>(LEKU_POI)

                showSnackbar("$latitude $longitude $address")
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
        }
    }


}
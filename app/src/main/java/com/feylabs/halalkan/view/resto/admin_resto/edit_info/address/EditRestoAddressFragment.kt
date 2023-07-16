package com.feylabs.halalkan.view.resto.admin_resto.edit_info.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.adevinta.leku.*
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.databinding.FragmentXrestoEditAddressBinding
import com.feylabs.halalkan.databinding.FragmentXrestoEditPhoneBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class EditRestoAddressFragment : BaseFragment(), OnMapReadyCallback {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoEditAddressBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModel<AdminRestoViewModel>()
    val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var mMap: GoogleMap

    override fun initUI() {
    }

    var targetLat = 0.0
    var targetLong = 0.0

    override fun initObserver() {
        viewModel.updateAddressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.title_success),
                        message = getString(R.string.message_data_updated_succesfully),
                        positiveAction = Pair("OK") {
                            findNavController().popBackStack()
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }
        }
        viewModel.detailRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    it.data?.let {
                        setupUiFromNetwork(it)
                    }
                }
            }
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.includeLoading.root.makeVisible()
        } else {
            binding.includeLoading.root.makeGone()
        }
    }

    private fun setupUiFromNetwork(data: RestoDetailResponse) {
        binding.etAddress.editText?.setText(data.data.detailResto.address)
        binding.etKecamatan.editText?.setText(data.data.detailResto.kecamatan)
        binding.etKelurahan.editText?.setText(data.data.detailResto.kelurahan)
    }

    override fun initAction() {
        binding.btnDirection.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withLocation(mainViewModel.getLat(), mainViewModel.getLong())
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(requireContext())

            startActivityForResult(locationPickerIntent, 212)
        }


        binding.btnSave.setOnClickListener {
            val address = binding.etAddress.editText?.text.toString()
            val kecamatan = binding.etKecamatan.editText?.text?:""
            val kelurahan = binding.etKelurahan.editText?.text?:""
            if (address.isEmpty()) {
                showSnackbar(getString(R.string.message_please_check_your_input))
            } else
                DialogUtils.showConfirmationDialog(
                    context = requireContext(),
                    title = getString(R.string.label_are_you_sure),
                    message = getString(R.string.message_resto_update_address),
                    positiveAction = Pair("OK") {
                        viewModel.updateRestoAddress(
                            getChoosenResto(),
                            lat = targetLat,
                            long = targetLong,
                            address = address,
                            kecamatan = kecamatan.toString(),
                            kelurahan = kelurahan.toString()
                        )
                    },
                    negativeAction = Pair(
                        getString(R.string.title_no),
                        { }),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun initData() {
        viewModel.getDetailResto(getChoosenResto())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoEditAddressBinding.inflate(inflater)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 212) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                val address = data.getStringExtra(LOCATION_ADDRESS)
                val lekuPoi = data.getParcelableExtra<LekuPoi>(LEKU_POI)

                targetLat = latitude
                targetLong = longitude

                binding.etAddress.editText?.setText(address)

                val location = LatLng(latitude, longitude)
                mMap.addMarker(MarkerOptions().position(location).title("Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val location = mainViewModel.getLatLong()
        mMap.addMarker(MarkerOptions().position(location).title("Lokasi Laporan"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))

        binding.etAddress?.editText?.setText(mainViewModel.getAddress())
    }


}
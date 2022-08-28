package com.feylabs.halalkan.view.resto.order.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adevinta.leku.*
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetOrderNotes
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.data.remote.reqres.order.DetailOrderResponse
import com.feylabs.halalkan.data.remote.reqres.order.OrderDataModel
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import com.feylabs.halalkan.databinding.FragmentOrderDetailRestoBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.resto.OrderUtility
import com.feylabs.halalkan.utils.resto.RestoUtility.getStatusColor
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.auth.AuthViewModel
import com.feylabs.halalkan.view.resto.OrderViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class DetailOrderRestoFragment : BaseFragment(), OnMapReadyCallback {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentOrderDetailRestoBinding? = null
    private val binding get() = _binding!!
    val authViewModel: AuthViewModel by viewModel()
    val mainViewModel: MainViewModel by sharedViewModel()
    val viewModel: OrderViewModel by viewModel()

    private val foodAdapter by lazy { DetailOrderItemAdapter(DetailOrderAdapterType.USER_REVIEW) }
    private lateinit var mMap: GoogleMap

    override fun initUI() {
        setupFoodAdapter()
    }


    private fun setupFoodAdapter() {
        binding.rvList.apply {
            adapter = foodAdapter
            layoutManager = setLayoutManagerLinear()
        }

        foodAdapter.setupAdapterInterface(object : DetailOrderItemAdapter.OrderInterface {
            override fun onchange() {
                checkItem()
            }
        })

        val lat = mainViewModel.liveLatLng.value?.lat
        val long = mainViewModel.liveLatLng.value?.long

        binding.btnPickLocation.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withLocation(lat ?: -99.0, long ?: -99.0)
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(requireContext())

            startActivityForResult(locationPickerIntent, 212)
        }



    }

    private fun setUserData(userModel: UserModel) {
        binding.etName?.editText?.setText(userModel.name)
        binding.etPhoneNumber?.editText?.setText(userModel.phoneNumber)
        binding.includeInfoProfile.labelInfoProfileUserName.text = userModel.name
        binding.includeInfoProfile.ivMainImage.loadImageFromURL(
            requireContext(),
            userModel.imgFullPath
        )


        binding.includeInfoProfile.labelInfoProfileContact.text = userModel.phoneNumber
        binding.includeInfoProfile.labelTime.text = userModel.email
    }

    private fun checkItem() {
        if (foodAdapter.itemCount == 0) {
            binding.stateNoFood.makeVisible()
        } else {
            binding.stateNoFood.makeGone()
        }
    }

    override fun initObserver() {
        viewModel.detailOrderLiveData.observe(viewLifecycleOwner){
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.let {
                        it.data.userObj?.let { userModel->
                            setUserData(userModel)
                        }
                        setDetailOrderData(it)
                    }
                }
            }
        }

    }

    private fun setDetailOrderData(orderResponse: DetailOrderResponse) {
        val rawData = orderResponse.data
        val userData = orderResponse.data.userObj
        rawData.orders?.let {
            foodAdapter.setWithNewData(it.toMutableList())
            foodAdapter.notifyDataSetChanged()
        }
        binding.labelStatus.text=rawData.statusDesc
        binding.labelStatus.setTextColor(rawData.statusId.getStatusColor());
        binding.labelTotalItems.text = "Total ${rawData.getTotalItem()} "+getString(R.string.label_item)
        binding.labelTotalPrice.text = rawData.getFormattedTotalPrice()

        binding.etAddress.editText?.setText(rawData.address)
        userData?.let {userModel->
            binding.etPhoneNumber.editText?.setText(userModel.phoneNumber)
            binding.etName.editText?.setText(userModel.name)
        }
    }

    private fun showLoading(b: Boolean) {
        if (b){
            binding.includeLoading.root.makeVisible()
        }else{
            binding.includeLoading.root.makeGone()
        }
    }

    override fun initAction() {
    }

    override fun initData() {
        viewModel.getDetailOrder(getOrderId())
    }

    private fun getOrderId(): Int {
        val obj = arguments?.getInt("orderId") ?: 0
        return obj
    }


    private fun showBottomSheetNotes(position: Int, model: FoodModelResponse) {
        val olz: (notes: String) -> Unit = { note ->
            foodAdapter.data[position].notes = note
            foodAdapter.notifyItemChanged(position)
            OrderUtility(requireContext()).changeNotes(model.id.toString(), note)
        }

        BottomSheetOrderNotes.instance(
            selectedAction = olz,
            objectId = model.id.toString(),
            existingNotes = model.notes
        ).show(getMFragmentManager(), BottomSheetOrderNotes().tag)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        val location = mainViewModel.getLatLong()
        mMap.addMarker(MarkerOptions().position(location).title("Lokasi Laporan"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))

        binding.etAddress?.editText?.setText(mainViewModel.getAddress())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 212) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                val address = data.getStringExtra(LOCATION_ADDRESS)
                val lekuPoi = data.getParcelableExtra<LekuPoi>(LEKU_POI)

                val location = LatLng(latitude, longitude)
                mMap.addMarker(MarkerOptions().position(location).title("Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderDetailRestoBinding.inflate(inflater)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
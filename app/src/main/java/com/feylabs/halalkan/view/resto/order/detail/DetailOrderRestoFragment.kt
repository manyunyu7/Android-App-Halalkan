package com.feylabs.halalkan.view.resto.order.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.adevinta.leku.*
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetOrderNotes
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.data.remote.reqres.driver.DriverObj
import com.feylabs.halalkan.data.remote.reqres.order.DetailOrderResponse
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import com.feylabs.halalkan.databinding.FragmentOrderDetailRestoBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.resto.OrderUtility
import com.feylabs.halalkan.utils.resto.RestoUtility.getStatusColor
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.auth.AuthViewModel
import com.feylabs.halalkan.view.direction.TurnByTurnExperienceActivity
import com.feylabs.halalkan.view.direction.TurnByTurnExperienceForDriver
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

    private fun setDriverData(driverObj: DriverObj) {
        val userModel = driverObj.userDriver
        binding.includeInfoDriver.labelInfoProfileUserName.text = userModel.name
        binding.includeInfoDriver.ivMainImage.loadImageFromURL(
            requireContext(),
            userModel.imgFullPath
        )
        binding.includeInfoDriver.labelInfoProfileContact.text = userModel.phoneNumber
        binding.includeInfoDriver.labelTime.text = userModel.email
    }


    private fun checkItem() {
        if (foodAdapter.itemCount == 0) {
            binding.stateNoFood.makeVisible()
        } else {
            binding.stateNoFood.makeGone()
        }
    }

    private fun showBottomSheetDriver() {
        val olz: (driverId: String) -> Unit = { driverId ->
            viewModel.delivOrder(getOrderId(), driverId.toIntOrNull() ?: -99)
        }

        BottomSheetChooseDriverFragment.instance(
            selectedAction = olz,
            restoId = getChoosenResto(),
        ).show(getMFragmentManager(), BottomSheetChooseDriverFragment().tag)
    }

    override fun initObserver() {
        viewModel.detailOrderLiveData.observe(viewLifecycleOwner) {
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

                        it.data.driverObj?.let {
                            binding.includeInfoDriver.root.makeVisible()
                            binding.separatorDriver.makeVisible()
                            binding.titleDriver.makeVisible()
                            setDriverData(it)
                        } ?: run {
                            binding.includeInfoDriver.root.makeGone()
                            binding.separatorDriver.makeGone()
                            binding.titleDriver.makeGone()
                        }

                        it.data.userObj?.let { userModel ->
                            setUserData(userModel)
                        }
                        setDetailOrderData(it)
                    }
                }
            }
        }
        viewModel.delivOrderLiveData.observe(viewLifecycleOwner) {
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
                    viewModel.getDetailOrder(getOrderId())
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.title_success),
                        message = getString(R.string.message_change_driver_success),
                        positiveAction = Pair("OK") {

                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }
        }
    }

    private fun setDetailOrderData(orderResponse: DetailOrderResponse) {
        val rawData = orderResponse.data
        val userData = orderResponse.data.userObj

        binding.includeOrderStatus.apply {
            labelOrderStatus.text = rawData.statusDesc
            labelOrderDate.text = rawData.createdAt
        }

        val location =
            LatLng(rawData.lat.toDoubleOrNull() ?: -18.0, rawData.long.toDoubleOrNull() ?: -9.0)
        mMap.addMarker(MarkerOptions().position(location).title("Lokasi Laporan"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))

        binding.btnDirection.setOnClickListener {
            goToDelivery(rawData)
        }

        binding.tvUserLocation.text = rawData.address

        rawData.orders?.let {
            foodAdapter.setWithNewData(it.toMutableList())
            foodAdapter.notifyDataSetChanged()
        }
        binding.labelStatus.text = rawData.statusDesc
        binding.labelStatus.setTextColor(rawData.statusId.getStatusColor());
        binding.labelTotalItems.text =
            "Total ${rawData.getTotalItem()} " + getString(R.string.label_item)
        binding.labelTotalPrice.text = rawData.getFormattedTotalPrice()

        binding.etAddress.editText?.setText(rawData.address)
        userData?.let { userModel ->
            binding.etPhoneNumber.editText?.setText(userModel.phoneNumber)
            binding.etName.editText?.setText(userModel.name)
        }

        //if order is not canceled
        if (rawData.statusId != 5) {
            when (rawData.statusId) {
                2 -> { // on cooking
                    binding.btnNext.text = getString(R.string.title_change_driver)
                    binding.btnNext.setOnClickListener {
                        showBottomSheetDriver()
                    }
                }
                3 -> { //on the way
                    binding.btnNext.text = getString(R.string.track_driver_position)
                    binding.btnNext.setOnClickListener {
                        showBottomSheetDriver()
                    }

                    if (muskoPref().getUserRole() == "4") {
                        binding.btnNext.text = getString(R.string.title_deliver_order)
                        binding.btnNext.setOnClickListener {

                        }
                    }
                }

                4 -> { //completed
                    binding.btnNext.text = getString(R.string.see_invoice)
                    binding.btnNext.setOnClickListener {
                        showBottomSheetDriver()
                    }
                }
            }
        }
    }

    private fun goToDelivery(rawData: DetailOrderResponse.Data) {
        MyPreference(requireContext()).saveLat(value = mainViewModel.getLat())
        MyPreference(requireContext()).saveLong(value = mainViewModel.getLong())

        findNavController().navigate(
            R.id.navigation_turnByTurnExperienceForDriver,
            bundleOf(
                "userData" to rawData.userObj,
                "orderId" to rawData.id.toString(),
                TurnByTurnExperienceForDriver.DESTINATION_LONG to rawData.long.toDoubleOrNull(),
                TurnByTurnExperienceForDriver.DESTINATION_LAT to rawData.lat.toDoubleOrNull(),
            )
        )
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.includeLoading.root.makeVisible()
        } else {
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
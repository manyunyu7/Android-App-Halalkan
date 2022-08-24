package com.feylabs.halalkan.view.resto.order.preview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.adevinta.leku.*
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetOrderNotes
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.data.remote.reqres.order.CreateCartPayload
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import com.feylabs.halalkan.databinding.FragmentOrderPreviewBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.resto.OrderUtility
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.auth.AuthViewModel
import com.feylabs.halalkan.view.resto.OrderViewModel
import com.feylabs.halalkan.view.resto.main.FoodAdapterType
import com.feylabs.halalkan.view.resto.main.RestoFoodAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class OrderPreviewFragment : BaseFragment(), OnMapReadyCallback {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentOrderPreviewBinding? = null
    private val binding get() = _binding!!
    val authViewModel: AuthViewModel by viewModel()
    val mainViewModel: MainViewModel by sharedViewModel()
    val viewModel: OrderViewModel by viewModel()

    var latLng: LatLng? = null

    private val foodAdapter by lazy { RestoFoodAdapter(FoodAdapterType.USER_REVIEW) }
    private lateinit var mMap: GoogleMap

    override fun initUI() {
        setupFoodAdapter()
        setupOrderCard()
        setupFoodItems()
    }


    private fun setupFoodItems() {
        val orders = OrderUtility(requireContext()).getListOrder()
        val tempList = mutableListOf<FoodModelResponse>()

        orders.forEachIndexed { index, orderLocalModel ->
            tempList.add(orderLocalModel.food)
        }

        foodAdapter.setWithNewData(tempList)
        foodAdapter.notifyDataSetChanged()
    }

    private fun setupFoodAdapter() {
        binding.rvList.apply {
            adapter = foodAdapter
            layoutManager = setLayoutManagerLinear()
        }

        foodAdapter.setupAdapterInterface(object : RestoFoodAdapter.OrderInterface {
            override fun onchange() {
                checkItem()
                setupOrderCard()
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



        foodAdapter.setupAdapterInterface(object : RestoFoodAdapter.NoteInterface {
            override fun onclick(model: FoodModelResponse, position: Int) {
                showBottomSheetNotes(position, model)
            }
        })

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
        viewModel.checkoutLiveData.observe(viewLifecycleOwner) {
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
                    showSnackbar(getString(R.string.order_submitted_to_restaurant))
                }
            }
        }

        authViewModel.userLiveData.observe(viewLifecycleOwner) {
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
                    it.data?.let {
                        setUserData(it.userModel)
                    }
                }
            }
        }
    }

    private fun showLoading(b: Boolean) {

    }

    override fun initAction() {
        binding.btnOrder.setOnClickListener {
            val address = binding.etAddress?.editText?.text.toString()
            val additionalAddress = binding.etAdditionalAddress?.editText?.text.toString()

            val latLng = mMap.cameraPosition.target

            val orders = mutableListOf<CreateCartPayload.Order>()

            OrderUtility(requireContext()).getListOrder().forEachIndexed { index, orderLocalModel ->
                orders.add(
                    CreateCartPayload.Order(
                        foodId = orderLocalModel.food.id,
                        notes = orderLocalModel.notes.toString(),
                        quantity = orderLocalModel.quantity
                    )
                )
            }
            showToast("restoId" + getChoosenResto().toString())

            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.please_double_check_your_order),
                message = getString(R.string.orders_cant_be_canceled),
                positiveAction = Pair("OK") {
                    viewModel.checkout(
                        CreateCartPayload(
                            restoId = getChoosenResto(),
                            address = address + "\n" + additionalAddress,
                            lat = latLng.latitude ?: mainViewModel.getLat(),
                            long = latLng.longitude ?: mainViewModel.getLong(),
                            orders = orders
                        )
                    )
                },
                negativeAction = Pair(
                    "No",
                    { }),
                autoDismiss = true,
                buttonAllCaps = false
            )

        }
    }

    override fun initData() {
        authViewModel.getUserProfile()
    }

    private fun setupOrderCard() {
        val obj = OrderUtility(requireContext()).getSummary()
        obj?.let {
            binding.containerSummary.makeVisible()
            binding.labelTotalPrice.text =
                it.getFormattedTotalPrice()
        } ?: run {
            binding.containerSummary.makeGone()
        }

        var text = ""
        var count = 0

        val items = OrderUtility(requireContext()).getListOrder()
        items.forEachIndexed { index, orderLocalModel ->
            count += 1
            val name = orderLocalModel.food.name
            val quantity = orderLocalModel.quantity
            text += "$count. $name x${quantity}   ${orderLocalModel.getFormattedFoodTotal()}\n"
        }

        binding.tvOrderedItems.text = text

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderPreviewBinding.inflate(inflater)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

}
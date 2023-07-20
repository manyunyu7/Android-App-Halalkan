package com.feylabs.halalkan.view.resto.admin_resto

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.adevinta.leku.*
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetOrderRejection
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.order.OrderStatusResponse
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryModel
import com.feylabs.halalkan.data.remote.reqres.order.resto.OrderByRestoPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.databinding.FragmentXrestoCurrentBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.ViewUtil.dpToPixels
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.OrderViewModel
import com.google.android.material.chip.Chip
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class CurrentRestaurantFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoCurrentBinding? = null
    private val binding get() = _binding!!

    val mainViewModel: MainViewModel by sharedViewModel()
    val viewModel by viewModel<AdminRestoViewModel>()
    val orderViewModel by viewModel<OrderViewModel>()

    private val PERMISSION_CODE_STORAGE = 1001

    private val mAdapter by lazy { RestoHistoryOrderAdapter() }

    override fun initUI() {
        arguments?.getString("name")?.let {
            binding.pageTitle.text = it
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack();
        }

        binding.rv.apply {
            adapter = mAdapter
            layoutManager = setLayoutManagerLinear()
        }

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    binding.btnLoadMore.makeVisible()
//                } else {
//                    binding.btnLoadMore.makeGone()
//                }
            }
        })

        mAdapter.setupAdapterInterface(object : RestoHistoryOrderAdapter.OrderRejectAcceptListener {
            override fun onAccept(model: OrderHistoryModel, adapterPosition: Int) {
                showAcceptConfirmation(model, adapterPosition)
            }

            override fun onReject(model: OrderHistoryModel, adapterPosition: Int) {
                showRejectConfirmation(model, adapterPosition)
            }

        })

        mAdapter.setupAdapterInterface(object : RestoHistoryOrderAdapter.ItemInterface {
            override fun onclick(model: OrderHistoryModel) {
                findNavController().navigate(
                    R.id.navigation_detailOrderRestoFragment,
                    bundleOf("orderId" to model.id)
                )
            }

        })
    }

    private fun showRejectConfirmation(model: OrderHistoryModel, adapterPosition: Int) {
        val olz: (notes: String) -> Unit = { note ->
            mAdapter.data[adapterPosition].statusId = 5
            mAdapter.notifyItemChanged(adapterPosition)
            orderViewModel.rejectOrder(orderId = model.id, reason = note)
        }

        BottomSheetOrderRejection.instance(
            selectedAction = olz,
            objectId = model.id.toString(),
            existingNotes = ""
        ).show(getMFragmentManager(), BottomSheetOrderRejection().tag)
    }

    private fun showAcceptConfirmation(model: OrderHistoryModel, adapterPosition: Int) {
        with(DialogUtils) {
            showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_order_changed_to_cooked),
                positiveAction = Pair("OK") {
                    orderViewModel.approveOrder(model.id)
                },
                negativeAction = Pair(
                    getString(R.string.title_no),
                    { showToast(getString(R.string.label_canceled)) }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }
    }

    override fun initObserver() {
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
                    setupRestoData(it.data)
                }
            }
        }

        orderViewModel.statusOrderLiveData.observe(viewLifecycleOwner) {
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
                        setupCategoryChip(it)
                    }
                }
            }
        }

        orderViewModel.approveOrderLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    orderViewModel.resetAcceptOrderState()
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }

                is Loading -> {
                    showLoading(true)
                }

                is Success -> {
                    orderViewModel.resetAcceptOrderState()
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.title_success),
                        message = getString(R.string.message_data_updated_succesfully),
                        positiveAction = Pair("OK") {
                            orderViewModel.getRestoHistory(restoId = getRestoId())
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    showLoading(false)
                }
            }
        }
        orderViewModel.rejectOrderLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    orderViewModel.resetRejectOrderState()
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }

                is Loading -> {
                    showLoading(true)
                }

                is Success -> {
                    orderViewModel.resetRejectOrderState()
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.title_success),
                        message = getString(R.string.message_data_updated_succesfully),
                        positiveAction = Pair("OK") {
                            orderViewModel.getRestoHistory(restoId = getRestoId())
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    showLoading(false)
                }
            }
        }


        orderViewModel.restoHistoryLiveData.observe(viewLifecycleOwner) {
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
                    setupOrderData(it.data)
                }
            }
        }


    }

    private fun setupCategoryChip(data: OrderStatusResponse) {
        val mdata = data.toMutableList()

        mdata.add(
            0,
            OrderStatusResponse.OrderStatusResponseItem(
                0, "All"
            )
        )
        mdata.forEachIndexed { index, statusOrder ->
            val chip = Chip(requireContext())
            chip.text = statusOrder.name
            chip.tag = statusOrder.id
            chip.isCheckable = true
            if (statusOrder.id.toString() == orderViewModel.getCurrentFilterStatus()) {
                chip.isChecked = true
            }

            chip.setTextColor(Color.parseColor("#000000"))
            chip.chipStrokeColor =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.chip_filter_selection_stroke_color
                    )
                )
            chip.chipStrokeWidth = 1.dpToPixels(requireContext())
            chip.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.chip_filter_selection_text_color
                    )
                )
            )
            chip.chipBackgroundColor =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.chip_filter_selection_color
                    )
                )
            binding.chipGroupCategory.addView(chip)
        }
    }

    private fun getSelectedCategoryId(): String? {
        val ids: List<Int> = binding.chipGroupCategory.getCheckedChipIds()
        var selected: String? = null
        ids.forEachIndexed { index, i ->
            val chip = view?.findViewById<Chip>(i)
            if (chip?.tag.toString() == "0") {
                selected = null
            } else
                selected = chip?.tag.toString()
        }
        return selected
    }

    private fun setupOrderData(data: OrderByRestoPaginationResponse?) {
        data?.let {
            val data = it.resto
            mAdapter.setWithNewData(data.toMutableList())
            mAdapter.notifyDataSetChanged()
            if (mAdapter.itemCount == 0)
                showEmptyOrder(true)
            else showEmptyOrder(false)
        } ?: run {
            showEmptyOrder(true)
        }
    }

    private fun showEmptyOrder(b: Boolean) {
        if (b) {
            binding.stateNoTransaction.makeVisible()
        } else {
            binding.stateNoTransaction.makeGone()
        }
    }

    private fun setupRestoData(data: RestoDetailResponse?) {
        data?.let {
            val resto = data.data.detailResto
            binding.labelInfoProfileUserName.text = resto.name
            binding.labelEmail.text = resto.address
            binding.labelPhone.text = resto.phoneNumber
            binding.ivMainImage.loadImageFromURL(requireContext(), resto.image_full_path)
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingAnim.makeVisible()
        } else {
            binding.loadingAnim.makeGone()
        }
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

        binding.menuOperatingHour.setOnClickListener {
            findNavController().navigate(R.id.navigation_editOperatingHoursFragment)
        }

        binding.menuAddress.setOnClickListener {
            findNavController().navigate(R.id.navigation_editRestoAddressFragment)
        }

        binding.chipGroupCategory.setOnCheckedChangeListener { group, checkedId ->
            orderViewModel.getRestoHistory(restoId = getRestoId(), status = getSelectedCategoryId())
        }


        setupMenuAction()
    }

    private fun setupMenuAction() {
        binding.apply {
            menuOperatingHour.build(
                "Operating Hour",
                getMuskoDrawable(R.drawable.ic_menu_resto_operating_hours)
            )

            menuTypeResto.build(
                "Type of Restaurant",
                getMuskoDrawable(R.drawable.ic_menu_resto_type)
            )

            menuLegal.setOnClickListener {
                findNavController().navigate(R.id.navigation_editRestoLegalFragment)
            }

            menuInfoBangunan.build(
                "Luas Tanah & Bangunan",
                getMuskoDrawable(R.drawable.ic_168_building)
            )

            menuInfoAirListrik.build(
                "Listrik, Air ",
                getMuskoDrawable(R.drawable.ic_168_flash)
            )

            menuKeramaianKendaraan.build(
                "Lalu Lintas, Parkir & Jalan Umum",
                getMuskoDrawable(R.drawable.ic_168_truck_delivery)
            )

            menuKeadaanLingkungan.build(
                "Keadaan Lingkungan Sekitar",
                getMuskoDrawable(R.drawable.ic_168_maps)
            )

            menuKeadaanLingkungan.setOnClickListener {
                findNavController().navigate(R.id.navigation_editRestoInfoLingkunganFragment)
            }

            menuInfoBangunan.setOnClickListener {
                findNavController().navigate(R.id.navigation_editRestoLtlBFragment)
            }

            menuKeramaianKendaraan.setOnClickListener {
                findNavController().navigate(R.id.navigation_editKeramaianLalinFragment)
            }

            menuInfoAirListrik.setOnClickListener {
                findNavController().navigate(R.id.navigation_editRestoListrikAir)
            }

            menuLegal.setOnClickListener {
                findNavController().navigate(R.id.navigation_editRestoLegalFragment)
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
//        orderViewModel.getAllOrderStatusLiveData()
        viewModel.getDetailResto(getRestoId())
        viewModel.getRestoCert()
        viewModel.getFoodType()
        orderViewModel.getRestoHistory(
            restoId = getRestoId(),
            page = 1,
            perPage = 10
        )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 212) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                val address = data.getStringExtra(LOCATION_ADDRESS)
                showSnackbar("$latitude $longitude $address")
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {

        }
    }


}


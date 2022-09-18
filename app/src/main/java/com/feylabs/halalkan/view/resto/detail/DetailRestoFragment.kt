package com.feylabs.halalkan.view.resto.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetGeneralAction
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetOrderNotes
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.data.remote.reqres.resto.AllFoodByRestoResponse
import com.feylabs.halalkan.data.remote.reqres.resto.FoodCategoryResponse
import com.feylabs.halalkan.data.remote.reqres.resto.FoodCategoryResponse.FoodCategoryResponseItem
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import com.feylabs.halalkan.databinding.FragmentDetailRestoBinding
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.NumberUtil.Companion.roundOffDecimal
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong
import com.feylabs.halalkan.utils.resto.OrderLocalModel
import com.feylabs.halalkan.utils.resto.OrderUtility
import com.feylabs.halalkan.view.auth.AuthViewModel
import com.feylabs.halalkan.view.direction.TurnByTurnExperienceActivity
import com.feylabs.halalkan.view.favorite.FavViewModel
import com.feylabs.halalkan.view.resto.RestoViewModel
import com.feylabs.halalkan.view.resto.main.RestoFoodAdapter
import com.like.LikeButton
import com.like.OnLikeListener
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class DetailRestoFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentDetailRestoBinding? = null
    private val binding get() = _binding!!

    val viewModel: RestoViewModel by viewModel()
    val authViewModel: AuthViewModel by viewModel()
    val favViewModel: FavViewModel by viewModel()

    val mainViewModel: MainViewModel by sharedViewModel()

    var initModel: RestoModelResponse? = null

    private val foodAdapter by lazy { RestoFoodAdapter() }
    private val categoryAdapter by lazy { FoodCategoryAdapter() }

    override fun initUI() {
        setupInitialUi()
        setupFoodAdapter()
        setupOrderCard()
        saveChoosenResto(getRestoId())
    }

    private fun setupFoodAdapter() {
        binding.rvList.apply {
            adapter = foodAdapter
            layoutManager = setLayoutManagerLinear()
        }

        binding.rvMenuCategory.apply {
            adapter = categoryAdapter
            layoutManager = setLayoutManagerHorizontal()
        }

        foodAdapter.setupAdapterInterface(object : RestoFoodAdapter.OrderInterface {
            override fun onchange() {
                setupOrderCard()
            }
        })
        foodAdapter.setupAdapterInterface(object : RestoFoodAdapter.NoteInterface {
            override fun onclick(model: FoodModelResponse, position: Int) {
                showBottomSheetNotes(position, model)
            }
        })

        categoryAdapter.setupAdapterInterface(object : FoodCategoryAdapter.ItemInterface {
            override fun onclick(model: FoodCategoryResponseItem) {
                //set current active menu
                viewModel.currentMenuTab.postValue(Pair(model.id, model.name))
            }
        })
    }

    private fun setupOrderCard() {
        val obj = OrderUtility(requireContext()).getSummary()
        obj?.let {
            binding.containerSummary.makeVisible()
            binding.labelTotalItems.text = "Total " + it.quantity.toString() + " item"
            binding.labelTotalPrice.text =
                it.getFormattedTotalPrice()
        } ?: run {
            binding.containerSummary.makeGone()
        }
    }


    private fun setupInitialUi(showError: Boolean = false, messageError: String = "") {
        initModel?.apply {
            binding.apply {
                binding.btnFavorite.isLiked = isFavorited
                binding.labelPageTitleTopbar.text = name
                binding.labelName.text = name
                binding.etAddressTop.text = address
                binding.etDistance.text =
                    calculateDistance(lat.toDoubleOrNull(), long.toDoubleOrNull())
                binding.etAddress.text = address
//                binding.etPhone.text = phone
                binding.etOperatingHours.text = "24 Jam"
//                binding.etFacilities.text = facilities.extractStringFromStringArrayBE()

                viewModel.targetLong.postValue(long.toDoubleOrNull())
                viewModel.targetLat.postValue(lat.toDoubleOrNull())

            }
        }
    }

    override fun initObserver() {
        viewModel.foodByCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {
                    showCenterLoadingIndicator(false)
                }
                is Error -> {
                    showToast(it.message.toString())
                    showCenterLoadingIndicator(false)
                }
                is Loading -> {
                    showCenterLoadingIndicator(false)
                }
                is Success -> {
                    if (!isAllMenu().not()) {
                        it.data?.let {
                            checkFoodSavedItem(it)
                            foodAdapter.setWithNewData(it)
                            foodAdapter.notifyDataSetChanged()
                            checkFoodAdapter()
                        }
                    }
                }
            }
        }

        viewModel.foodCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data?.let {
                        setupFoodCategory(it)
                    }
                }
                is Loading -> {
                    showLoadingListFood(true)
                }
                else -> {
                    showLoadingListFood(false)
                }
            }
        }

        viewModel.restoFoodByCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {
                    showLoadingListFood(false)
                }
                is Error -> {
                    showToast(it.message.toString())
                    showLoadingListFood(false)
                }
                is Loading -> {
                    showLoadingListFood(true)
                }
                is Success -> {
                    showLoadingListFood(false)
                    it.data?.let {
                        foodAdapter.setWithNewData(it)
                        foodAdapter.notifyDataSetChanged()
                        checkFoodAdapter()
                    }
                }
            }

        }

        viewModel.currentMenuTab.observe(viewLifecycleOwner) {
            val foodCategoryId = it.first
            categoryAdapter.setActiveMenu(it.first)
            if (isAllMenu()) {
                viewModel.getAllFoodByResto(getRestoId())
            } else {
                viewModel.getRestoFoodByCategory(getRestoId(), foodCategoryId.toString())
            }
        }

        viewModel.allFoodLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Error -> {
                    showLoadingListFood(false)
                }
                is Loading -> {
                    showLoadingListFood(true)
                }
                is Success -> {
                    showLoadingListFood(false)
                    it.data?.let { data ->
                        if (isAllMenu()) {
                            foodAdapter.setWithNewData(data)
                            foodAdapter.notifyDataSetChanged()
                            checkFoodAdapter()
                        }
                    } ?: run {
                        //TODO
                    }
                }
            }
        }


        viewModel.detailRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Error -> {
                    showCenterLoadingIndicator(false)
                }
                is Loading -> {
                    showCenterLoadingIndicator(true)
                }
                is Success -> {
                    showCenterLoadingIndicator(false)
                    it.data?.let { masjidDetailResponse ->
                        setupDetailFromNetwork(masjidDetailResponse)
                    } ?: run {
                        setupInitialUi(showError = true, messageError = "")
                    }
                }
            }
        }

    }

    private fun checkFoodSavedItem(it: AllFoodByRestoResponse) {
        var isSomethingRemoved = false
        var removedItem = "\n"
        it.forEachIndexed { index, foodModelResponse ->
            if (foodModelResponse.isVisible == 0) {
                if (OrderUtility(requireContext()).isItemAlreadyInserted(foodModelResponse.id.toString())) {
                    OrderUtility(requireContext()).removeItem(foodModelResponse.id.toString())
                    isSomethingRemoved = true
                    removedItem = "${index + 1}. ${foodModelResponse.name}\n"
                }
            }
        }

        if (isSomethingRemoved) {
            DialogUtils.showErrorDialog(
                context = requireContext(),
                title = getString(R.string.title_sorry),
                message = getString(R.string.sorry_no_available)
                        + "\n"
                        + getString(R.string.removed_items_are)
                        + "\n"
                        + removedItem,
                positiveAction = Pair("OK") {
                },
                autoDismiss = true,
                buttonAllCaps = false
            )
        }
    }


    private fun checkFoodAdapter() {
        if (foodAdapter.itemCount == 0) {
            binding.stateNoFood.makeVisible()
        } else {
            binding.stateNoFood.makeGone()
        }
    }

    private fun setupFoodCategory(data: FoodCategoryResponse) {
        val tempData = data
        tempData.add(
            0, FoodCategoryResponseItem(id = -99, "All")
        )
        categoryAdapter.setWithNewData(tempData)
        categoryAdapter.notifyDataSetChanged()
        setAllMenu()
    }

    private fun setAllMenu() = viewModel.currentMenuTab.postValue(Pair(-99, ""))
    private fun isAllMenu() = viewModel.currentMenuTab.value?.first == -99

    private fun setupDetailFromNetwork(restoDetailResponse: RestoDetailResponse) {
        binding.apply {
            restoDetailResponse.data.detailResto.apply {
                val resto = this
                foodAdapter.isRestoClosed = this.isRestoScheduleOpen.not()
                binding.labelPageTitleTopbar.text = name
                binding.labelName.text = name
                binding.etCategoryTop.text = "-"
                binding.etAddressTop.text = address
                binding.etDistance.text =
                    calculateDistance(lat.toDoubleOrNull(), long.toDoubleOrNull())
                binding.etAddress.text = address
                binding.etKategori.text = "-"
                binding.etPhone.text = phoneNumber
                binding.etFacilities.text = "-"
                binding.etOperatingHours.text = getOperatingHours()

                setupRestoPhotoData(restoDetailResponse.data.photos)

                binding.includeRatingInfo.apply {
                    reviewScore.text =
                        restoDetailResponse.data.totalRating.toString()
                    tvReviewCount.text = restoDetailResponse.data.totalReview.toString()
                }

                viewModel.targetLong.postValue(long.toDoubleOrNull())
                viewModel.targetLat.postValue(lat.toDoubleOrNull())
            }
        }
    }

    private fun setupRestoPhotoData(photos: List<String>) {
        val tempList = mutableListOf<CustomViewPhotoModel>()
        photos.forEachIndexed { index, photo ->
            tempList.add(
                CustomViewPhotoModel(
                    url = photo
                )
            )
        }
        binding.btnSeeAllPhotos.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_photoListFragment, bundleOf(
                    "photos" to photos
                )
            )
        }
        binding.ipImagePreviewSlider.replaceAllImage(tempList)
    }


    override fun initAction() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnMaps.setOnClickListener {
            startActivity(
                Intent(requireActivity(), TurnByTurnExperienceActivity::class.java)
                    .putExtra(
                        TurnByTurnExperienceActivity.DESTINATION_LONG,
                        viewModel.targetLong.value
                    )
                    .putExtra(
                        TurnByTurnExperienceActivity.DESTINATION_LAT,
                        viewModel.targetLat.value
                    )
            )
        }

        if (muskoPref().isLoggedIn().not()) {
            binding.btnFavorite.makeInvisible()
        }

        binding.btnFavorite.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                if (muskoPref().isLoggedIn())
                    favViewModel.addFavResto(getRestoId())
                else
                    showSnackbar(getString(R.string.u_shoud_logged_in))
            }

            override fun unLiked(likeButton: LikeButton) {
                if (muskoPref().isLoggedIn())
                    favViewModel.removeFavResto(getRestoId())
                else
                    showSnackbar(getString(R.string.u_shoud_logged_in))
            }
        })

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_detailRestoFragment_to_navigation_orderPreviewFragment)
        }

        binding.etActionCall.setOnClickListener {
            val number = binding.labelPhone.text.toString()
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + number) //change the number
            startActivity(callIntent)
        }

        binding.labelAddress.setOnClickListener {
            startActivity(
                Intent(requireActivity(), TurnByTurnExperienceActivity::class.java)
                    .putExtra(
                        TurnByTurnExperienceActivity.DESTINATION_LONG,
                        viewModel.targetLong.value
                    )
                    .putExtra(
                        TurnByTurnExperienceActivity.DESTINATION_LAT,
                        viewModel.targetLat.value
                    )
            )
        }

        binding.btnWriteReview.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_restoReviewFragment, bundleOf(
                    "id" to initModel?.id.toString()
                )
            )
        }
    }

    override fun initData() {
        initModel = arguments?.getParcelable<RestoModelResponse>("data")
        viewModel.getDetailResto(getRestoId())
        viewModel.getFoodCategoryOnResto(getRestoId())
        viewModel.getAllFoodByResto(getRestoId())
        authViewModel.getUserProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailRestoBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculateDistance(lat: Double?, long: Double?): CharSequence? {
        val mLat = lat ?: -99.0
        val mLong = long ?: -99.0
        val myLocation = mainViewModel.liveLatLng.value
        val distance = LocationUtils.calculateDistance(
            loc1 = myLocation,
            loc2 = MyLatLong(mLat, mLong)
        )
        return distance.roundOffDecimal() + " Km"
    }

    private fun showLoadingListFood(value: Boolean) {
        if (value) {
            binding.rvList.makeGone()
            binding.loadingListFood.makeVisible()
        } else {
            binding.rvList.makeVisible()
            binding.loadingListFood.makeGone()
        }
    }

    private fun showCenterLoadingIndicator(value: Boolean) {
        if (value)
            binding.loadingCenterProgressBar.makeVisible()
        else
            binding.loadingCenterProgressBar.makeGone()
    }

    private fun showBottomSheetNotes(position: Int, model: FoodModelResponse) {
        val olz: (notes: String) -> Unit = { note ->
            foodAdapter.data[position].notes = note
            foodAdapter.notifyItemChanged(position)
            OrderUtility(requireContext()).changeNotes(model.id.toString(), note)
        }

        BottomSheetOrderNotes.instance(
            title = getString(R.string.add_note_to_dish),
            selectedAction = olz,
            objectId = model.id.toString(),
            existingNotes = model.notes
        ).show(getMFragmentManager(), BottomSheetOrderNotes().tag)
    }


    private fun getRestoId(): String {
        initModel?.let {
            return it.id.toString()
        } ?: run {
            return ""
        }
    }
}
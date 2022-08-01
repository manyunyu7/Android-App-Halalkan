package com.feylabs.halalkan.view.resto.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import com.feylabs.halalkan.databinding.FragmentDetailPrayerBinding
import com.feylabs.halalkan.databinding.FragmentDetailRestoBinding
import com.feylabs.halalkan.utils.NumberUtil.Companion.roundOffDecimal
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong
import com.feylabs.halalkan.view.direction.TurnByTurnExperienceActivity
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import com.feylabs.halalkan.view.resto.RestoViewModel
import com.feylabs.halalkan.view.resto.main.RestoFoodAdapter
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class DetailRestoFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentDetailRestoBinding? = null
    private val binding get() = _binding!!

    val viewModel: RestoViewModel by viewModel()

    val mainViewModel: MainViewModel by sharedViewModel()

    var initModel: RestoModelResponse? = null

    private val foodAdapter by lazy { RestoFoodAdapter() }

    override fun initUI() {
        setupInitialUi()
        setupFoodAdapter()
        setupMenuTab()
    }

    private fun setupFoodAdapter() {
        binding.rvList.apply {
            adapter = foodAdapter
            layoutManager = setLayoutManagerLinear()
        }
    }

    private fun setupMenuTab() {
        binding.menuAlcohol.apply {
            tabMenuName.text = "Alcohol"
            root.setOnClickListener {
                viewModel.currentMenuTab.value = Pair(3, "Alcohol")
            }
        }

        binding.menuHalalFood.apply {
            tabMenuName.text = "Halal"
            root.setOnClickListener {
                viewModel.currentMenuTab.value = Pair(1, "Halal")
            }
        }

        binding.menuNonHalalFood.apply {
            tabMenuName.text = "Non Halal"
            root.setOnClickListener {
                viewModel.currentMenuTab.value = Pair(2, "Non Halal")
            }
        }
    }

    private fun setupInitialUi(showError: Boolean = false, messageError: String = "") {
        initModel?.apply {
            binding.apply {
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
        mainViewModel.liveLatLng.observe(viewLifecycleOwner) {

        }

        viewModel.restoFoodByCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {
                    showCenterLoadingIndicator(false)
                }
                is Error -> {
                    showCenterLoadingIndicator(false)
                }
                is Loading -> {
                    showCenterLoadingIndicator(true)
                }
                is Success -> {
                    showCenterLoadingIndicator(false)
                    it.data?.let {
                        foodAdapter.setWithNewData(it.data.toMutableList())
                        foodAdapter.notifyDataSetChanged()
                    }
                }
            }

        }

        viewModel.currentMenuTab.observe(viewLifecycleOwner) {
            val foodCategoryId = it.first.toString()
            when (it.second) {
                "Halal" -> {
                    viewModel.getRestoFoodByCategory(getRestoId(), foodCategoryId)
                    binding.menuHalalFood.activeIndicator.makeVisible()
                    binding.menuAlcohol.activeIndicator.makeGone()
                    binding.menuNonHalalFood.activeIndicator.makeGone()
                }
                "Non Halal" -> {
                    viewModel.getRestoFoodByCategory(getRestoId(), foodCategoryId)
                    binding.menuHalalFood.activeIndicator.makeGone()
                    binding.menuAlcohol.activeIndicator.makeGone()
                    binding.menuNonHalalFood.activeIndicator.makeVisible()
                }
                "Alcohol" -> {
                    viewModel.getRestoFoodByCategory(getRestoId(), foodCategoryId)
                    binding.menuHalalFood.activeIndicator.makeGone()
                    binding.menuAlcohol.activeIndicator.makeVisible()
                    binding.menuNonHalalFood.activeIndicator.makeGone()
                }
            }
        }

        viewModel.detailRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
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

    private fun setupDetailFromNetwork(restoDetailResponse: RestoDetailResponse) {
        binding.apply {
            restoDetailResponse.data.detailResto.apply {
                val resto = this
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
        binding.ipImagePreviewSlider.replaceAllImage(tempList)
    }


    override fun initAction() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.etActionCall.setOnClickListener {
            val number = binding.labelPhone.text.toString()
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + number) //change the number
            startActivity(callIntent)
        }

        binding.btnFavorite.setOnClickListener {
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
                R.id.navigation_masjidReviewNewFragment, bundleOf(
                    "id" to initModel?.id.toString()
                )
            )
        }
    }

    override fun initData() {
        viewModel.currentMenuTab.value = (Pair(0, "Halal"))
        initModel = arguments?.getParcelable<RestoModelResponse>("data")
        viewModel.getDetailResto(initModel?.id.toString())
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

    private fun showCenterLoadingIndicator(value: Boolean) {
        if (value)
            binding.loadingCenterProgressBar.makeVisible()
        else
            binding.loadingCenterProgressBar.makeGone()
    }

    private fun getRestoId(): String {
        initModel?.let {
            return it.id.toString()
        } ?: run {
            return ""
        }
    }
}
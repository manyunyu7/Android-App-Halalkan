package com.feylabs.halalkan.view.prayer.detail

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
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidModelResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.databinding.FragmentDetailPrayerBinding
import com.feylabs.halalkan.utils.NumberUtil.Companion.roundOffDecimal
import com.feylabs.halalkan.utils.StringUtil.extractStringFromStringArrayBE
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong
import com.feylabs.halalkan.view.direction.TurnByTurnExperienceActivity
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class DetailMasjidFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentDetailPrayerBinding? = null
    private val binding get() = _binding!!

    val viewModel: PrayerRoomViewModel by viewModel()

    val mainViewModel : MainViewModel by sharedViewModel()

    var initModel: MasjidModelResponse? = null

    override fun initUI() {
        setupInitialUi()
    }

    private fun setupInitialUi(showError: Boolean = false, messageError: String = "") {
//        binding.labelMasjidName.text = "blabla"

        initModel?.apply {
            binding.apply {
                binding.labelPageTitleTopbar.text = name
                binding.labelName.text = name
                binding.etCategoryTop.text = categoryName
                binding.etAddressTop.text = address
                binding.etDistance.text = calculateMasjidDistance(lat.toDoubleOrNull(), long.toDoubleOrNull())
                binding.etAddress.text = address
                binding.etKategori.text = categoryName
                binding.etPhone.text = phone
                binding.etOperatingHours.text = getOperatingHours()
                binding.etFacilities.text = facilities.extractStringFromStringArrayBE()

                viewModel.targetLong.postValue(long.toDoubleOrNull())
                viewModel.targetLat.postValue(lat.toDoubleOrNull())

            }
        }
    }

    override fun initObserver() {

        mainViewModel.liveLatLng.observe(viewLifecycleOwner){

        }

        viewModel.masjidDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MuskoResource.Default -> {}
                is MuskoResource.Error -> {
                    showCenterLoadingIndicator(false)
                }
                is MuskoResource.Loading -> {
                    showCenterLoadingIndicator(true)
                }
                is MuskoResource.Success -> {
                    showCenterLoadingIndicator(false)
                    it.data?.let { masjidDetailResponse ->
                        val data = masjidDetailResponse.data
                        if (data.isNotEmpty()) {
                            val firstData = masjidDetailResponse.data[0]
                            setupMasjidDetailFromNetwork(firstData)
                        } else {
                            setupInitialUi(showError = true, messageError = "")
                            // TODO() : handle empty masjid
                        }
                    }
                }
            }
        }

        viewModel.masjidPhotoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MuskoResource.Default -> {
                    binding.ipImagePreviewSlider.setLoading(true)
                }
                is MuskoResource.Error -> {
                    binding.ipImagePreviewSlider.setLoading(false)
                }
                is MuskoResource.Loading -> {
                    binding.ipImagePreviewSlider.setLoading(true)
                }
                is MuskoResource.Success -> {
                    binding.ipImagePreviewSlider.setLoading(false)
                    it.data?.let {
                        setupMasjidPhotoData(it)
                    } ?: run {
                        binding.ipImagePreviewSlider.setLoading(false, isEmpty = true)
                    }
                }
            }
        }
    }

    private fun setupMasjidDetailFromNetwork(masjidDetailResponse: MasjidModelResponse) {
        binding.apply {
            masjidDetailResponse.apply {
                binding.labelPageTitleTopbar.text = name
                binding.labelName.text = name
                binding.etCategoryTop.text = categoryName
                binding.etAddressTop.text = address
                binding.etDistance.text = calculateMasjidDistance(lat.toDoubleOrNull(), long.toDoubleOrNull())
                binding.etAddress.text = address
                binding.etKategori.text = categoryName
                binding.etPhone.text = phone
                binding.etFacilities.text = facilities.extractStringFromStringArrayBE()
                binding.etOperatingHours.text=getOperatingHours()

                binding.includeRatingInfo.apply {
                    reviewScore.text=masjidDetailResponse.review_avg
                    tvReviewCount.text=masjidDetailResponse.review_count
                }

                viewModel.targetLong.postValue(long.toDoubleOrNull())
                viewModel.targetLat.postValue(lat.toDoubleOrNull())
            }
        }
    }

    private fun setupMasjidPhotoData(masjidPhotos: MasjidPhotosResponse) {
        val tempList = mutableListOf<CustomViewPhotoModel>()
        masjidPhotos.forEachIndexed { index, photo ->
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
            startActivity(Intent(requireActivity(),TurnByTurnExperienceActivity::class.java)
                .putExtra(TurnByTurnExperienceActivity.DESTINATION_LONG,viewModel.targetLong.value)
                .putExtra(TurnByTurnExperienceActivity.DESTINATION_LAT,viewModel.targetLat.value)
            )
        }

        binding.btnWriteReview.setOnClickListener {
            findNavController().navigate(R.id.navigation_masjidReviewNewFragment, bundleOf(
                "id" to initModel?.id.toString()
            ))
        }
    }

    override fun initData() {
        initModel = arguments?.getParcelable<MasjidModelResponse>("data")
        viewModel.getMasjidPhoto(initModel?.id.toString())
        viewModel.getDetailMasjid(initModel?.id.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailPrayerBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculateMasjidDistance(lat: Double?, long: Double?): CharSequence? {
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
}
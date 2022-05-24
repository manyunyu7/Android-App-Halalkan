package com.feylabs.halalkan.view.prayer.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.customview.AskPermissionDialog
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.masjid.DataMasjid
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidDetailResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.databinding.FragmentDetailPrayerBinding
import com.feylabs.halalkan.utils.PermissionCommandUtil
import com.feylabs.halalkan.utils.PermissionUtil
import com.feylabs.halalkan.utils.PermissionUtil.Companion.isBlocked
import com.feylabs.halalkan.utils.PermissionUtil.Companion.isNotGranted
import com.feylabs.halalkan.utils.StringUtil.extractElementArrayFromStringArrayBE
import com.feylabs.halalkan.utils.StringUtil.extractStringFromStringArrayBE
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class DetailMasjidFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentDetailPrayerBinding? = null
    private val binding get() = _binding!!

    val viewModel: PrayerRoomViewModel by viewModel()

    var initModel: DataMasjid? = null

    override fun initUI() {
        setupInitialUi()
    }

    private fun setupInitialUi(showError: Boolean = false, messageError: String = "") {
        initModel?.apply {
            binding.apply {
                binding.labelPageTitleTopbar.text = name
                binding.labelMasjidName.text = name
                binding.etCategoryTop.text = categoryName
                binding.etAddressTop.text = address
                binding.etDistance.text = calculateMasjidDistance(lat, long)
                binding.etAddress.text = address
                binding.labelPhone.text = phone
                binding.etKategori.text = categoryName
                binding.etActionCall.text = phone
                binding.etOperatingHours.text = getOperatingHours()
                binding.etFacilities.text = facilities.extractStringFromStringArrayBE()

                val photo = mutableListOf(
                    CustomViewPhotoModel(
                        name = "Photo 1",
                        url = ""
                    ),
                    CustomViewPhotoModel(
                        name = "Photo 2",
                        url = "https://i.pinimg.com/736x/4c/8f/49/4c8f49756ce273966029dd0a9e9381cd.jpg"
                    ),
                    CustomViewPhotoModel(
                        name = "Photo 3",
                        url = "https://i.pinimg.com/564x/fc/be/ca/fcbeca2a32f542a1bd9c4f7b74f6d9b8.jpg"
                    )
                )
                binding.ipImagePreviewSlider.replaceAllImage(photo)
            }
        }
    }

    override fun initObserver() {
        viewModel.masjidDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {}
                is QumparanResource.Error -> {
                    showCenterLoadingIndicator(false)
                }
                is QumparanResource.Loading -> {
                    showCenterLoadingIndicator(true)
                }
                is QumparanResource.Success -> {
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
                is QumparanResource.Default -> {
                    binding.ipImagePreviewSlider.setLoading(true)
                }
                is QumparanResource.Error -> {
                    binding.ipImagePreviewSlider.setLoading(false)
                }
                is QumparanResource.Loading -> {
                    binding.ipImagePreviewSlider.setLoading(true)
                }
                is QumparanResource.Success -> {
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

    private fun setupMasjidDetailFromNetwork(masjidDetailResponse: DataMasjid) {
        binding.apply {
            masjidDetailResponse.apply {
                binding.labelPageTitleTopbar.text = name
                binding.labelMasjidName.text = name
                binding.etCategoryTop.text = categoryName
                binding.etAddressTop.text = address
                binding.etDistance.text = calculateMasjidDistance(lat, long)
                binding.etAddress.text = address
                binding.labelPhone.text = phone
                binding.etKategori.text = categoryName
                binding.etActionCall.text = phone
                binding.etFacilities.text = facilities.extractStringFromStringArrayBE()
                binding.etOperatingHours.text=getOperatingHours()
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
            findNavController().popBackStack()
        }
    }

    override fun initData() {
        initModel = arguments?.getParcelable<DataMasjid>("data")
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

    private fun calculateMasjidDistance(lat: String, long: String): CharSequence? {
        return ""
    }

    private fun showCenterLoadingIndicator(value: Boolean) {
        if (value)
            binding.loadingCenterProgressBar.makeVisible()
        else
            binding.loadingCenterProgressBar.makeGone()
    }
}
package com.feylabs.halalkan.view.prayer.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.databinding.FragmentDetailPrayerBinding
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.utils.base.BaseFragment


class DetailMasjidFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentDetailPrayerBinding? = null
    private val binding get() = _binding!!

    var initModel: MasjidResponseWithoutPagination.DataMasjid? = null

    override fun initUI() {
        initModel?.apply {
            binding.apply {
                binding.labelPageTitleTopbar.text = name
                binding.labelMasjidName.text = name
                binding.etCategoryTop.text = categoryName
                binding.etAddressTop.text= address
                binding.etDistance.text = "200Km"
                binding.etAddress.text = address
                binding.etPhone.text = phone
                binding.etKategori.text = categoryName
                binding.etActionCall.text = phone

                val photo = mutableListOf(
                    CustomViewPhotoModel(
                        name="Photo 1",
                        url = "https://i.pinimg.com/564x/53/8f/b6/538fb6e8e1b28070e6b9aab1256e9282.jpg"
                    ),
                    CustomViewPhotoModel(
                        name="Photo 2",
                        url = "https://i.pinimg.com/736x/4c/8f/49/4c8f49756ce273966029dd0a9e9381cd.jpg"
                    ),
                    CustomViewPhotoModel(
                        name="Photo 3",
                        url = "https://i.pinimg.com/564x/fc/be/ca/fcbeca2a32f542a1bd9c4f7b74f6d9b8.jpg"
                    )
                )
                binding.ipImagePreviewSlider.replaceAllImage(photo)
            }
        }
    }

    override fun initObserver() {
    }

    override fun initAction() {
    }

    override fun initData() {
        initModel = arguments?.getParcelable<MasjidResponseWithoutPagination.DataMasjid>("data")
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

}
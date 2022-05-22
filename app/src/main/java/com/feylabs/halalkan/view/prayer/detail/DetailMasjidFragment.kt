package com.feylabs.halalkan.view.prayer.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
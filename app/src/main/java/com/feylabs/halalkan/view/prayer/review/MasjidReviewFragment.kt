package com.feylabs.halalkan.view.prayer.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewsResponse
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.databinding.FragmentReviewMasjidBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class MasjidReviewFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentReviewMasjidBinding? = null
    private val binding get() = _binding!!

    private val viewModel : PrayerRoomViewModel by viewModel()

    override fun initUI() {
    }

    private fun getMasjidId(): String {
        return arguments?.getString("id").toString()
    }

    override fun initObserver() {
        viewModel.masjidReviewsLiveData.observe(viewLifecycleOwner){
            when(it){
                is QumparanResource.Default -> {
                }
                is QumparanResource.Error ->{

                }
                is QumparanResource.Loading ->{

                }
                is QumparanResource.Success ->{
                    it.data?.let {response->
                        setupReviewFromNetwork(response)
                    }
                }
            }
        }
    }

    private fun setupReviewFromNetwork(response: MasjidReviewsResponse) {
        binding.startStats.setStarCount(response.reviewCount.avg)
        binding.startStats.setStartBarUi(response.reviewCount)
    }

    override fun initAction() {
    }

    override fun initData() {
        viewModel.getMasjidReview(getMasjidId())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewMasjidBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
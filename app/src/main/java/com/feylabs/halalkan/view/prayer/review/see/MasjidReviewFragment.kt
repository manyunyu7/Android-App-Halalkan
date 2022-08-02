package com.feylabs.halalkan.view.prayer.review.see

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.databinding.FragmentReviewMasjidBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import org.koin.android.viewmodel.ext.android.viewModel


@Deprecated(message = "Deprecated", replaceWith = ReplaceWith("Masjid Review New Fragment"))
class MasjidReviewFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentReviewMasjidBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PrayerRoomViewModel by viewModel()

    private val mAdapter by lazy { MasjidReviewAdapter() }

    override fun initUI() {
        setupAdapterRv()
    }

    private fun setupAdapterRv() {
        binding.rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }


    }

    private fun getMasjidId(): String {
        return arguments?.getString("id").toString()
    }

    override fun initObserver() {
        viewModel.masjidReviewsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MuskoResource.Default -> {
                }
                is MuskoResource.Error -> {
                }
                is MuskoResource.Loading -> {
                }
                is MuskoResource.Success -> {
                    it.data?.let { response ->
                        setupReviewFromNetwork(response)
                    }
                }
            }
        }
    }

    private fun setupReviewFromNetwork(response: MasjidReviewPaginationResponse) {
        val reviewRes = response.reviews
        reviewRes.let {
            if (it.data.isEmpty()) {
                showToast("Tidak Ada Data")
            } else {
                mAdapter.setWithNewData(it.data.toMutableList())
                mAdapter.notifyDataSetChanged()
            }
        }

        binding.startStats.setStarCount(response.reviewCount.avg)
        binding.startStats.setStartBarUi(response.reviewCount)
    }

    override fun initAction() {
    }

    override fun initData() {
        viewModel.getMasjidReview(getMasjidId(), page = 1)
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
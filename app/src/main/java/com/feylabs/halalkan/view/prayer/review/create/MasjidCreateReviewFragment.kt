package com.feylabs.halalkan.view.prayer.review.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.databinding.FragmentCreateReviewMasjidBinding
import com.feylabs.halalkan.databinding.FragmentReviewNewMasjidBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class MasjidCreateReviewFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentCreateReviewMasjidBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PrayerRoomViewModel by viewModel()


    override fun initUI() {
    }


    private fun getMasjidId(): String {
        return arguments?.getString("id") ?: ""
    }

    override fun initObserver() {
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
        _binding = FragmentCreateReviewMasjidBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
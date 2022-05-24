package com.feylabs.halalkan.view.prayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.PostResponse
import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.databinding.FragmentHomeBinding
import com.feylabs.halalkan.databinding.FragmentNewHomeBinding
import com.feylabs.halalkan.databinding.FragmentPrayerMainBinding
import com.feylabs.halalkan.utils.TimeUtil
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.home.HomeViewModel
import com.feylabs.halalkan.view.home.ListPostsAdapter
import com.feylabs.halalkan.view.new_home.RestaurantHomeUIModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class PrayerMainFragment : BaseFragment() {

    val viewModel: HomeViewModel by viewModel()
    val mainViewModel: MainViewModel by sharedViewModel()
    private var _binding: FragmentPrayerMainBinding? = null

    private val mAdapter by lazy { ListPrayerRoomAdapter() }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun initAction() {
        binding.menuPrayerQibla.setOnClickListener {
            findNavController().navigate(R.id.navigation_qiblaFragment)
        }

        binding.menuPrayerRoom.setOnClickListener {
            findNavController().navigate(R.id.navigation_listAndSearchPrayerRoomFragment)
        }

        binding.menuPrayerTime.setOnClickListener {
            findNavController().navigate(R.id.navigation_qiblaFragment)
        }
    }

    override fun initData() {
        fetchPrayerTime()
        fetchAllMasjid()
    }

    private fun fetchAllMasjid() {
        viewModel.fetchAllMasjid()
    }

    private fun fetchPrayerTime() {
        viewModel.fetchPrayerTimeSingle(
            latitude = mainViewModel.liveLatLng.value?.lat ?: 0.0,
            longitude = mainViewModel.liveLatLng.value?.long ?: 0.0,
            method = "11",
            time = TimeUtil.getCurrentTimeUnix()
        )
    }

    override fun initUI() {
        initRecyclerView()
        initAdapter()
    }

    private fun initAdapter() {
        mAdapter.setupAdapterInterface(object : ListPrayerRoomAdapter.ItemInterface {
            override fun onclick(model: PrayerRoomListUIModel) {
               // TODO("Not yet implemented")
            }
        })
    }

    private fun initRecyclerView() {
        binding.rvResto.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = mAdapter
        }
    }

    override fun initObserver() {

        mainViewModel.liveAddress.observe(viewLifecycleOwner){

        }

        mainViewModel.liveLatLng.observe(viewLifecycleOwner){

        }

        viewModel.prayerTimeSingleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    binding.shimmerViewJadwal.showShimmer(true)
                }
                is QumparanResource.Error -> {
                    binding.shimmerViewJadwal.hideShimmer()
                }
                is QumparanResource.Loading -> {
                    binding.shimmerViewJadwal.showShimmer(true)
                }
                is QumparanResource.Success -> {
                    binding.shimmerViewJadwal.hideShimmer()
                    it.data?.let { response ->
                        setupPrayerTimeLiveData(response)
                    }
                }
            }
        }

    }

    private fun setupPrayerTimeLiveData(response: PrayerTimeAladhanSingleDateResponse) {
        val data = response.data
        data.timings.let { timings ->
            binding.includeDatePray.apply {
                txtTimeAshar.text = timings.asr
                txtTimeDhuhur.text = timings.dhuhr
                txtTimeIsya.text = timings.isha
                txtTimeSubuh.text = timings.fajr
                txtTimeMagrib.text = timings.maghrib
            }
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            viewGone(binding.rvResto)
//            viewVisible(binding.loading)
        } else {
            viewVisible(binding.rvResto)
//            viewGone(binding.loading)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPrayerMainBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
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
import com.feylabs.halalkan.data.remote.reqres.masjid.DataMasjid
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.databinding.FragmentHomeBinding
import com.feylabs.halalkan.databinding.FragmentNewHomeBinding
import com.feylabs.halalkan.databinding.FragmentPrayerMainBinding
import com.feylabs.halalkan.utils.TimeUtil
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong
import com.feylabs.halalkan.utils.masjid.MasjidUtility.renderWithDistanceModel
import com.feylabs.halalkan.view.home.HomeViewModel
import com.feylabs.halalkan.view.home.ListPostsAdapter
import com.feylabs.halalkan.view.new_home.ListMasjidAdapter
import com.feylabs.halalkan.view.new_home.RestaurantHomeUIModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class PrayerMainFragment : BaseFragment() {

    val viewModel: HomeViewModel by viewModel()
    val mainViewModel: MainViewModel by sharedViewModel()
    private var _binding: FragmentPrayerMainBinding? = null


    private val mMosqueAdapter by lazy { ListMasjidAdapter() }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun initAction() {

        binding.btnSeeAllMasjid.setOnClickListener {
            findNavController().navigate(R.id.navigation_allMasjidFragment)
        }

        binding.btnExit.setOnClickListener {
            findNavController().popBackStack()
        }
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
        mMosqueAdapter.setupAdapterInterface(object : ListMasjidAdapter.ItemInterface {
            override fun onclick(model: DataMasjid) {
                findNavController().navigate(
                    R.id.navigation_detailMasjidFragment,
                    bundleOf("data" to model)
                )
            }
        })
    }

    private fun initRecyclerView() {
        binding.rvMasjid.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = mMosqueAdapter
        }
    }

    override fun initObserver() {

        mainViewModel.liveAddress.observe(viewLifecycleOwner) {
            binding.tvAddress.text = it.toString()
        }

        mainViewModel.liveKecamatan.observe(viewLifecycleOwner) {
            binding.tvCityName.text = it.toString()
        }

        mainViewModel.liveLatLng.observe(viewLifecycleOwner){

        }

        viewModel.allMasjidLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoadingMosque(false)
                }
                is QumparanResource.Error -> {
                    showLoadingMosque(false)
                }
                is QumparanResource.Loading -> {
                    showLoadingMosque(true)
                }
                is QumparanResource.Success -> {
                    it.data?.let { response ->
                        setupMosqueData(response)
                    }
                }
            }
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

    private fun setupMosqueData(response: MasjidResponseWithoutPagination) {

        var initialData = response.data.toMutableList()

        if(LocationUtils.checkIfLocationSet(mainViewModel.liveLatLng.value)){
            initialData = initialData.renderWithDistanceModel(
                myLocation = mainViewModel.liveLatLng.value ?: MyLatLong(-99.0,-99.0)
            )
        }

        mMosqueAdapter.setWithNewData(initialData)
        mMosqueAdapter.notifyDataSetChanged()
        showLoadingMosque(false)
    }



    private fun showLoadingMosque(b: Boolean) {
        if (b) {
            viewGone(binding.rvMasjid)
            viewVisible(binding.loadingMosque.root)
        } else {
            viewVisible(binding.rvMasjid)
            viewGone(binding.loadingMosque.root)
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
package com.feylabs.halalkan.view.prayer.all_prayer_room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.masjid.DataMasjid
import com.feylabs.halalkan.data.remote.reqres.masjid.pagination.AllMasjidPaginationResponse
import com.feylabs.halalkan.databinding.FragmentAllPrayerRoomBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong
import com.feylabs.halalkan.utils.masjid.MasjidUtility.renderWithDistanceModel
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class AllMasjidFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentAllPrayerRoomBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PrayerRoomViewModel by viewModel()
    private val mainViewModel: MainViewModel by sharedViewModel()

    var scrolled = 0

    private fun fetchAllMasjid() {
        // initial is start with one
        viewModel.getMasjidWithPagination(1)
    }

    companion object{
        const val SCROLLED_POSITION = "DASDC"
    }

    private val mAdapter by lazy { AllMasjidAdapter() }

    override fun onResume() {
        super.onResume()
        val index = MyPreference(requireContext()).getPrefInt(SCROLLED_POSITION) ?: 0
        binding.rvList.smoothScrollBy(0,index)
    }

    override fun initUI() {
        setupAdapterRv()
    }

    private fun setupAdapterRv() {
        binding.rvList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            isSaveEnabled=true
        }

        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrolled += dy
                MyPreference(requireContext()).save(SCROLLED_POSITION,scrolled)
            }
        })


        mAdapter.setupAdapterInterface(object : AllMasjidAdapter.ItemInterface {
            override fun onclick(model: DataMasjid) {
                findNavController().navigate(
                    R.id.action_navigation_allMasjidFragment_to_navigation_detailMasjidFragment,
                    bundleOf("data" to model)
                )
            }

            override fun loadMore(page: Int) {
                val currentPage = mAdapter.page
                viewModel.getMasjidWithPagination(page = currentPage + 1)
            }
        })
    }

    override fun initObserver() {
        viewModel.masjidPaginateLiveData.observe(viewLifecycleOwner) {
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
                    showLoadingMosque(false)
                    it.data?.let { response ->
                        setupMosqueData(response)
                    }
                }
            }
        }
    }

    private fun setupMosqueData(response: AllMasjidPaginationResponse) {
        var initialData = response.data.data.toMutableList()

        if (LocationUtils.checkIfLocationSet(mainViewModel.liveLatLng.value)) {
            showToast("Lat Long Available")
            initialData = initialData.renderWithDistanceModel(
                myLocation = mainViewModel.liveLatLng.value ?: MyLatLong(-99.0, -99.0),
                sortByNearest = false, limit = 999
            )
        } else {
            showToast(mainViewModel.liveLatLng.value.toString())
        }

        val dataResponse = response.data
        dataResponse.let {
            if (it.data.isEmpty()) {
                showToast("Tidak Ada Data")
            } else {
                if (it.currentPage == 1) {
                    mAdapter.page = it.currentPage
                    mAdapter.setWithNewData(initialData)
                } else {
                    if (it.lastPage == it.currentPage) {
                        showToast("Anda Berada di Halaman Terakhir")
                    } else {
                        mAdapter.addNewData(initialData)
                        mAdapter.page = it.currentPage
                    }
                }
            }
        }
    }

    private fun showLoadingMosque(b: Boolean) {
        if (b) {
            viewVisible(binding.loadingAnim)
        } else {
            viewGone(binding.loadingAnim)
        }
    }


    override fun initAction() {
        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    mAdapter.loadMore()
                }
            }
        })
    }

    override fun initData() {
        if (mAdapter.itemCount == 0) {
            fetchAllMasjid()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllPrayerRoomBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
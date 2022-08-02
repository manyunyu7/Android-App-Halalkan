package com.feylabs.halalkan.view.prayer.list_and_search

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
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidModelResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong
import com.feylabs.halalkan.utils.masjid.MasjidUtility.renderWithDistanceModel
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class ListAndSearchPrayerRoomFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentListAndSearchPrayerRoomBinding? = null
    private val binding get() = _binding!!

    val viewModel: PrayerRoomViewModel by viewModel()
    val mainViewModel: MainViewModel by sharedViewModel()

    private val adapter by lazy {
        ListPrayerRoomAdapter()
    }


    override fun initUI() {
        binding.rvList.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }

        adapter.adapterInterface = object  : ListPrayerRoomAdapter.ItemInterface{

            override fun onclick(model: MasjidModelResponse) {
                findNavController().navigate(
                    R.id.navigation_detailMasjidFragment,
                    bundleOf("data" to model)
                )
            }

        }
    }

    override fun initObserver() {
        viewModel.masjidLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is MuskoResource.Default -> {

                }
                is MuskoResource.Error -> {
                    showToast(it.data.toString() + it.message.toString())
                }
                is MuskoResource.Loading -> {
                    showToast("Loadingg")
                }
                is MuskoResource.Success -> {
                    it.data?.let { masjidData ->
                        setupData(masjidData)
                    }
                }
            }
        })
    }

    private fun setupData(response: MasjidResponseWithoutPagination) {
        var initialData = response.data.toMutableList()

        if(LocationUtils.checkIfLocationSet(mainViewModel.liveLatLng.value)){
            initialData = initialData.renderWithDistanceModel(
                myLocation = mainViewModel.liveLatLng.value ?: MyLatLong(-99.0,-99.0)
            )
        }

        adapter.setWithNewData(initialData)
        adapter.notifyDataSetChanged()
    }

    override fun initAction() {
    }

    override fun initData() {
        viewModel.getAllMosque()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListAndSearchPrayerRoomBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
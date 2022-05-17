package com.feylabs.halalkan.view.prayer.list_and_search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.mapper.masjid.MasjidDataMapper.toListMasjidUI
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class ListAndSearchPrayerRoomFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentListAndSearchPrayerRoomBinding? = null
    private val binding get() = _binding!!

    val viewModel: PrayerRoomViewModel by viewModel()

    private val adapter by lazy {
        ListPrayerRoomAdapter()
    }


    override fun initUI() {
        binding.rvList.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }
    }

    override fun initObserver() {
        viewModel.masjidLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is QumparanResource.Default -> {

                }
                is QumparanResource.Error -> {
                    showToast(it.data.toString() + it.message.toString())
                }
                is QumparanResource.Loading -> {
                    showToast("Loadingg")
                }
                is QumparanResource.Success -> {
                    it.data?.let { masjidData ->
                        setupData(masjidData)
                    }
                }
            }
        })
    }

    private fun setupData(masjidData: MasjidResponseWithoutPagination) {
        val data = masjidData.toListMasjidUI()
        adapter.setWithNewData(data)
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
package com.feylabs.halalkan.view.prayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.PostResponse
import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.databinding.FragmentHomeBinding
import com.feylabs.halalkan.databinding.FragmentNewHomeBinding
import com.feylabs.halalkan.databinding.FragmentPrayerMainBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.home.HomeViewModel
import com.feylabs.halalkan.view.home.ListPostsAdapter
import com.feylabs.halalkan.view.new_home.RestaurantHomeUIModel
import org.koin.android.viewmodel.ext.android.viewModel

class PrayerMainFragment : BaseFragment() {

    val viewModel: HomeViewModel by viewModel()
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
        viewModel.fetchUsers()
    }

    override fun initUI() {
        initRecyclerView()
        initAdapter()
    }

    private fun initAdapter() {
        mAdapter.setupAdapterInterface(object : ListPrayerRoomAdapter.ItemInterface {
            override fun onclick(model: PrayerRoomListUIModel) {
                TODO("Not yet implemented")
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
        viewModel.postLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is QumparanResource.Default -> {
                }
                is QumparanResource.Error -> {
                    showToast(it.message.toString())
                }
                is QumparanResource.Loading -> {
                }
                is QumparanResource.Success -> {
                }
            }
        })
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
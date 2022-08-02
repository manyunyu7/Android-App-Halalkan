package com.feylabs.halalkan.view.resto.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.FoodTypeResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestaurantCertificationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import com.feylabs.halalkan.databinding.FragmentRestoMainBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong
import com.feylabs.halalkan.utils.resto.RestoUtility.renderWithDistanceModel
import com.feylabs.halalkan.view.resto.RestoViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class RestoMainFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentRestoMainBinding? = null
    private val binding get() = _binding!!

    val certificationAdapter by lazy { CertificationAdapter() }
    val foodTypeAdapter by lazy { FoodTypeAdapter() }
    val nearbyRestoAdapter by lazy { RestoMainAdapter() }

    val mainViewModel: MainViewModel by sharedViewModel()
    val viewModel by viewModel<RestoViewModel>()

    override fun initUI() {
        binding.rvCertification.adapter = certificationAdapter
        binding.rvCertification.apply {
            layoutManager = setLayoutManagerHorizontal()
        }

        binding.rvRestoNearby.apply {
            layoutManager =setLayoutManagerHorizontal()
            adapter = nearbyRestoAdapter
        }

        nearbyRestoAdapter.setupAdapterInterface(object :RestoMainAdapter.ItemInterface{
            override fun onclick(model: RestoModelResponse) {
                findNavController().navigate(
                    R.id.navigation_detailRestoFragment,
                    bundleOf("data" to model)
                )
            }

        })

        binding.rvTypeOfFood.apply {
            layoutManager = setLayoutManagerGridHorizontal(1)
            adapter = foodTypeAdapter
        }
    }

    override fun initObserver() {

        viewModel.allRestoRawLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {}
                is Loading -> {}
                is Success -> {
                    setupRestoData(it.data)
                }
            }
        }

        viewModel.foodTypeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {}
                is Loading -> {}
                is Success -> {
                    setupFoodTypeData(it.data)
                }
            }
        }

        viewModel.certLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {}
                is Loading -> {}
                is Success -> {
                    setupCertData(it.data)
                }
            }
        }
    }

    private fun setupRestoData(data: AllRestoNoPagination?) {
        data?.let {
            var initialData = it.data.toMutableList()

            if (LocationUtils.checkIfLocationSet(mainViewModel.liveLatLng.value)) {
                initialData = initialData.renderWithDistanceModel(
                    myLocation = mainViewModel.liveLatLng.value ?: MyLatLong(-99.0, -99.0),
                    sortByNearest = true, limit = 999
                )
            }

            nearbyRestoAdapter.apply {
                setWithNewData(initialData)
                notifyDataSetChanged()
            }
        }

    }

    private fun setupFoodTypeData(data: FoodTypeResponse?) {
        data?.let {
            foodTypeAdapter.apply {
                setWithNewData(it)
                notifyDataSetChanged()
            }
        }
    }

    private fun setupCertData(data: RestaurantCertificationResponse?) {
        data?.let {
            certificationAdapter.apply {
                setWithNewData(it)
                notifyDataSetChanged()
            }
        }
    }

    override fun initAction() {
    }

    override fun initData() {
        viewModel.getAllRestoRaw()
        viewModel.getFoodType()
        viewModel.getRestoCert()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRestoMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
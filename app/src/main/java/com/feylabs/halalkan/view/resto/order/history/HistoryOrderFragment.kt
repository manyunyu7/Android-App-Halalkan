package com.feylabs.halalkan.view.resto.order.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryModel
import com.feylabs.halalkan.databinding.FragmentHistoryOrderBinding
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.OrderViewModel
import com.feylabs.halalkan.view.resto.main.FoodAdapterType
import com.feylabs.halalkan.view.resto.main.RestoFoodAdapter
import org.koin.android.viewmodel.ext.android.viewModel


class HistoryOrderFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentHistoryOrderBinding? = null
    private val binding get() = _binding!!

    val viewModel: OrderViewModel by viewModel()

    private val mAdapter by lazy { HistoryOrderAdapter() }

    override fun initUI() {
        setupBottomNav()
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rv.apply {
            adapter = mAdapter
            layoutManager = setLayoutManagerLinear()
        }

        mAdapter.setupAdapterInterface(object : HistoryOrderAdapter.ItemInterface {
            override fun onclick(model: OrderHistoryModel) {

            }

        })

    }

    private fun setupBottomNav() {
        binding.bottomNav.apply {
            setBottomMenuActive(tvMenuHistory)
            setBottomMenuActive(ivMenuHistory)
            btnMenuRestoHome.setOnClickListener {
                findNavController().popBackStack(R.id.navigation_restoMainFragment,false)
            }
            btnMenuOrderHistory.setOnClickListener {
            }
        }
    }


    override fun initObserver() {
        viewModel.orderHistoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.orderData?.let {
                        mAdapter.setWithNewData(it.toMutableList())
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    private fun showLoading(b: Boolean) {
        if (b){
            binding.loadingAnim.makeVisible()
        }else{
            binding.loadingAnim.makeGone()
        }
    }

    override fun initAction() {
    }

    override fun initData() {
        viewModel.getOrderHistory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryOrderBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
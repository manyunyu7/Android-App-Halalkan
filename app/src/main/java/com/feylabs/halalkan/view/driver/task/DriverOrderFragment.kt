package com.feylabs.halalkan.view.resto.order.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.forum.AllForumPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.order.DriverOrderPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryModel
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryResponse
import com.feylabs.halalkan.databinding.FragmentDriverOrderBinding
import com.feylabs.halalkan.databinding.FragmentHistoryOrderBinding
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.driver.task.DriverOrderAdapter
import com.feylabs.halalkan.view.resto.DriverViewModel
import com.feylabs.halalkan.view.resto.OrderViewModel
import com.feylabs.halalkan.view.resto.RestoViewModel
import com.feylabs.halalkan.view.resto.main.FoodAdapterType
import com.feylabs.halalkan.view.resto.main.RestoFoodAdapter
import org.koin.android.viewmodel.ext.android.viewModel


class DriverOrderFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentDriverOrderBinding? = null
    private val binding get() = _binding!!

    val viewModel: OrderViewModel by viewModel()
    val driverViewModel: DriverViewModel by viewModel()

    private var currentPage = 1
    var totalPage = 0

    private val mAdapter by lazy { DriverOrderAdapter() }

    override fun initUI() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rv.apply {
            adapter = mAdapter
            layoutManager = setLayoutManagerLinear()
        }

        mAdapter.setupAdapterInterface(object : DriverOrderAdapter.ItemInterface {
            override fun onclick(model: OrderHistoryModel) {
                findNavController().navigate(
                    R.id.navigation_detailOrderRestoFragment,
                    bundleOf("orderId" to model.id)
                )
            }
        })

    }

    private fun loadNextPage() {
        val currentPage = mAdapter.page
        if (currentPage >= currentPage)
            showSnackbar("You are on the last page")
        else
            driverViewModel.getDriverOrder(page = currentPage + 1)
    }

    override fun initObserver() {
        driverViewModel.driverOrderLiveData.observe(viewLifecycleOwner) {
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
                    it?.data?.let {
                        setupDataFromNetwork(it)
                    }
                }
            }
        }
    }

    private fun setupDataFromNetwork(it: DriverOrderPaginationResponse) {
        val reviewRes = it
        reviewRes.let {
            if (it.data.isEmpty()) {
                if (it.currentPage == 1) {
                    showEmptyLayout(true)
                }
            } else {
                showEmptyLayout(false)
                totalPage = it.lastPage

                if (it.currentPage == 1) {
                    // if this is a first page
                    mAdapter.page = it.currentPage
                    mAdapter.setWithNewData(it.data.toMutableList())

                } else {
                    mAdapter.page = it.currentPage
                    mAdapter.addNewData(it.data.toMutableList())
                    binding.rv.scrollToPosition(mAdapter.data.size)
                }

                // check if this is last page....
                // if so hide the load more button
                if (it.currentPage == it.total) {
                    binding.btnLoadMore.makeGone()
                } else {
                    binding.btnLoadMore.makeVisible()
                }
            }
        }
    }

    private fun showEmptyLayout(b: Boolean) {
        if (b) {
            binding.layoutEmptyState.makeVisible()
        } else {
            binding.layoutEmptyState.makeGone()
        }
    }


    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingAnim.makeVisible()
        } else {
            binding.loadingAnim.makeGone()
        }
    }

    override fun initAction() {
        binding.btnLogout.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_logged_out),
                positiveAction = Pair("OK") {
                    muskoPref().clearPreferences()
                    findNavController().navigate(R.id.navigation_newHomeFragment)
                },
                negativeAction = Pair(
                    getString(R.string.title_no),
                    { showToast(getString(R.string.label_canceled)) }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }
    }

    override fun initData() {
        driverViewModel.getDriverOrder()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDriverOrderBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
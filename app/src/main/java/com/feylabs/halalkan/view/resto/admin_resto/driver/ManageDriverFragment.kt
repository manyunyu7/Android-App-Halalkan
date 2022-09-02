package com.feylabs.halalkan.view.resto.admin_resto.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.data.remote.reqres.resto.FoodCategoryResponse
import com.feylabs.halalkan.databinding.FragmentXrestoCategoryBinding
import com.feylabs.halalkan.databinding.FragmentXrestoManageDriverBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.DriverViewModel
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.feylabs.halalkan.view.resto.admin_resto.menu_category.ManageFoodCategoryAdapter
import org.koin.android.viewmodel.ext.android.viewModel


class ManageDriverFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoManageDriverBinding? = null
    private val binding get() = _binding!!
    private val PERMISSION_CODE_STORAGE = 1001

    val viewModel by viewModel<DriverViewModel>()
    private val mAdapter by lazy { ManageDriverAdapter() }

    override fun initUI() {
        binding.rv.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = setLayoutManagerLinear()
        }

        mAdapter.setupAdapterInterface(object : ManageDriverAdapter.ItemInterface {
            override fun onclick(model: UserModel) {
                findNavController().navigate(
                    R.id.navigation_addEditDriverFragment,
                    bundleOf("driverId" to model.id.toString())
                )
            }

        })


    }

    override fun initObserver() {
        viewModel.driverOnRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showLoading(false)
                    it.data?.let {
                        mAdapter.setWithNewData(it.getDrivers())
                        mAdapter.notifyDataSetChanged()
                    }
                }
                is Default -> {}
                is Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
            }
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
        binding.btnCreate.setOnClickListener {
            findNavController().navigate(R.id.navigation_addEditDriverFragment)
        }
    }

    private fun getRestoId(): String {
        return getChoosenResto()
    }

    override fun initData() {
        viewModel.getDriverOnResto(getRestoId())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoManageDriverBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
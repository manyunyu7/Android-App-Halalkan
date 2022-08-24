package com.feylabs.halalkan.view.resto.admin_resto.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.FoodCategoryResponse
import com.feylabs.halalkan.databinding.FragmentXrestoCategoryBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.feylabs.halalkan.view.resto.admin_resto.menu_category.ManageFoodCategoryAdapter
import org.koin.android.viewmodel.ext.android.viewModel


class ManageDriverFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoCategoryBinding? = null
    private val binding get() = _binding!!
    private val PERMISSION_CODE_STORAGE = 1001

    val viewModel by viewModel<AdminRestoViewModel>()
    private val mAdapter by lazy { ManageFoodCategoryAdapter() }

    override fun initUI() {
        binding.rv.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = setLayoutManagerLinear()
        }

        mAdapter.setupAdapterInterface(object : ManageFoodCategoryAdapter.ItemInterface {
            override fun onclick(model: FoodCategoryResponse.FoodCategoryResponseItem) {
            }
        })
    }

    override fun initObserver() {
        viewModel.foodCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showLoading(false)
                    it.data?.let {
                        mAdapter.setWithNewData(it)
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
        if(b){
            binding.loadingAnim.makeVisible()
        }else{
            binding.loadingAnim.makeGone()
        }
    }

    override fun initAction() {
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.navigation_addEditCategoryFragment)
        }
    }

    private fun getRestoId(): String {
        return getChoosenResto()
    }

    override fun initData() {
        viewModel.getFoodCategoryOnResto(getRestoId())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
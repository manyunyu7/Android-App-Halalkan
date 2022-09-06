package com.feylabs.halalkan.view.resto.main.type_food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.resto.FoodTypeResponse
import com.feylabs.halalkan.databinding.FragmentCategoryProductsBinding
import com.feylabs.halalkan.databinding.FragmentTypeFoodBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.resto.RestoViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class TypeFoodFragment : BaseFragment() {

    private var _binding: FragmentTypeFoodBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { TypeFoodAdapter() }

    val viewModel: RestoViewModel by viewModel()

    override fun initUI() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter.setupAdapterInterface(object : TypeFoodAdapter.ItemInterface {
            override fun onclick(model: FoodTypeResponse.FoodTypeResponseItem) {
            }
        })
        binding.rv.let {
            it.layoutManager = setLayoutManagerGridVertical(3)
            it.adapter = mAdapter
        }

    }

    override fun initObserver() {
        viewModel.foodTypeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {

                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString())
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it?.data?.let {
                        setupUiFromNetwork(it)
                    }
                }
            }
        }
    }

    private fun setupUiFromNetwork(it: FoodTypeResponse) {
        mAdapter.setWithNewData(it)
        mAdapter.notifyDataSetChanged()
    }

    private fun showLoading(b: Boolean) {
        if (b)
            binding.loadingAnim.makeVisible()
        else binding.loadingAnim.makeGone()
    }

    override fun initAction() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }


    }

    override fun initData() {
        if (mAdapter.itemCount == 0)
            viewModel.getFoodType()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTypeFoodBinding.inflate(inflater)
        return binding.root
    }

}
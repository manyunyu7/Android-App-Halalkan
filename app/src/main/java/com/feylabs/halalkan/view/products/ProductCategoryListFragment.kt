package com.feylabs.halalkan.view.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.product.ProductCategoryResponse
import com.feylabs.halalkan.databinding.FragmentCategoryProductsBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel


class ProductCategoryListFragment : BaseFragment() {

    private var _binding: FragmentCategoryProductsBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { ProductCategoryListAdapter() }

    val viewModel: ProductViewModel by viewModel()

    override fun initUI() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter.setupAdapterInterface(object : ProductCategoryListAdapter.ItemInterface {
            override fun onclick(model: ProductCategoryResponse.ProductCategoryResponseItem) {
                findNavController().navigate(
                    R.id.navigation_productListFragment,
                    bundleOf(
                        "categoryId" to model.id.toString(),
                        "pageTitle" to model.name
                    )
                )
            }
        })
        binding.rv.let {
            it.layoutManager = setLayoutManagerGridVertical(3)
            it.adapter = mAdapter
        }

    }

    override fun initObserver() {
        viewModel.productCategoryLiveData.observe(viewLifecycleOwner) {
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

    private fun setupUiFromNetwork(it: ProductCategoryResponse) {
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

        binding.searchBox.root.setOnClickListener {
            findNavController().navigate(R.id.navigation_searchProductFragment)
        }
    }

    override fun initData() {
        if (mAdapter.itemCount == 0)
            viewModel.getProductCategory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryProductsBinding.inflate(inflater)
        return binding.root
    }

}
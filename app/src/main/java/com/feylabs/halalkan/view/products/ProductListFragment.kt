package com.feylabs.halalkan.view.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.product.ProductListPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.product.ProductModelResponse
import com.feylabs.halalkan.databinding.FragmentCategoryProductsBinding
import com.feylabs.halalkan.databinding.FragmentListProductBinding
import com.feylabs.halalkan.utils.StringUtil.orMuskoEmpty
import com.feylabs.halalkan.utils.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel


class ProductListFragment : BaseFragment() {

    private var _binding: FragmentListProductBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { ProductListAdapter() }

    var totalPage = 0

    val viewModel: ProductViewModel by viewModel()

    override fun initUI() {
        binding.btnLoadMore.makeGone()
        initRecyclerView()
        arguments?.getString("pageTitle")?.let {
            binding.pageTitle.text = it
        }
    }

    private fun initRecyclerView() {
        mAdapter.setupAdapterInterface(object : ProductListAdapter.ItemInterface {
            override fun onclick(model: ProductModelResponse) {
                findNavController().navigate(
                    R.id.navigation_productDetailFragment,
                    bundleOf("productId" to model.id.toString())
                )
            }
        })
        binding.rv.let {
            it.layoutManager = setLayoutManagerGridVertical(3)
            it.adapter = mAdapter
        }
    }

    override fun initObserver() {
        viewModel.productsLiveData.observe(viewLifecycleOwner) {
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

    private fun setupUiFromNetwork(response: ProductListPaginationResponse) {
        val reviewRes = response
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
            binding.stateEmpty.makeVisible()
        } else {
            binding.stateEmpty.makeGone()
        }
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

        binding.btnLoadMore.setOnClickListener {
            loadNextPage()
        }
    }

    private fun loadNextPage() {
        val currentPage = mAdapter.page
        if (currentPage >= totalPage)
            showSnackbar("You are on the last page")
        else
            viewModel.getProductOnCategory(page = currentPage + 1, categoryId = getCategoryId())
    }


    override fun initData() {
        viewModel.getProductOnCategory(categoryId = getCategoryId())
    }

    fun getCategoryId(): String {
        return arguments?.getString("categoryId").orMuskoEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListProductBinding.inflate(inflater)
        return binding.root
    }

}
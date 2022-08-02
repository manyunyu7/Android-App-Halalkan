package com.feylabs.halalkan.view.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.MuskoResource
import com.feylabs.halalkan.data.remote.reqres.product.ProductCateogryResponse
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.databinding.FragmentCategoryProductsBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.mapbox.maps.extension.style.expressions.dsl.generated.product
import org.koin.android.viewmodel.ext.android.viewModel


class CategoryProductsFragment : BaseFragment() {

    private lateinit var newArrayList: ArrayList<CategoryProductsModel>
    private var _binding: FragmentCategoryProductsBinding? = null
    private val binding get() = _binding!!

    val viewModel: ProductViewModel by viewModel()

    override fun initUI() {
        initRecyclerView()
    }

    private val adapter by lazy { CategoryProductAdapter() }

    private fun initRecyclerView() {

        adapter.setupAdapterInterface(object : CategoryProductAdapter.ItemInterface{
            override fun onclick(model: ProductCateogryResponse.ProductCateogryResponseItem) {
                findNavController().navigate(R.id.categoryListFragment)
            }

        })
        binding.categoryproductRecycler.let {
            it.layoutManager = setLayoutManagerGridVertical(3)
            it.adapter = adapter
        }
    }

    override fun initObserver() {
        viewModel.ProductCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MuskoResource.Default -> {

                }
                is MuskoResource.Error -> {
                    showToast(it.message.toString())

                }
                is MuskoResource.Loading -> {
                    showToast("Loading")
                }

                is MuskoResource.Success -> {
                    it?.data?.let {
                        adapter.setWithNewData(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun initAction() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }



    override fun initData() {
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
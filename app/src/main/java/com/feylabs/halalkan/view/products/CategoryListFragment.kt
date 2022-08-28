package com.feylabs.halalkan.view.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.databinding.FragmentCategoryProductsBinding
import com.feylabs.halalkan.databinding.FragmentListProductsBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.mapbox.maps.extension.style.expressions.dsl.generated.product


class CategoryListFragment : BaseFragment() {

    private lateinit var newArrayList: ArrayList<CategoryProductsModel>
    private var _binding: FragmentListProductsBinding? = null
    private val binding get() = _binding!!

    override fun initUI() {
        initCategoryProductsList()
        initRecyclerView()
    }

    fun initCategoryProductsList() {
        newArrayList = arrayListOf<CategoryProductsModel>()
        getUserData()
    }

    private fun getUserData() {
        newArrayList.add(CategoryProductsModel(R.drawable.menu_home_restaurant,"restaurant"))
        newArrayList.add(CategoryProductsModel(R.drawable.menu_home_scan,"scan"))
        newArrayList.add(CategoryProductsModel(R.drawable.menu_home_prayer,"prayer"))
    }

    private fun initRecyclerView() {
        val madapter = CategoryProductAdapter(newArrayList)
        madapter.setupAdapterInterface(object : CategoryProductAdapter.ItemInterface{
            override fun onclick(model: CategoryProductsModel) {
                findNavController().navigate(R.id.categoryDetailsFragment)
            }

        })
        binding.categoryproductRecycler.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = madapter
        }

    }

    override fun initObserver() {
    }

    override fun initAction() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListProductsBinding.inflate(inflater)
        return binding.root
    }

}
package com.feylabs.halalkan.view.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.databinding.FragmentCategoryProductsBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.mapbox.maps.extension.style.expressions.dsl.generated.product


class CategoryProductsFragment : BaseFragment() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<CategoryProductsModel>
    lateinit var product_category : Array<String>
    lateinit var img_category : Array<Int>

    private var _binding: FragmentCategoryProductsBinding? = null
    private val binding get() = _binding!!


    override fun initUI() {
        initCategoryProductsList()
        initRecyclerView()
    }

    fun initCategoryProductsList() {
        img_category = arrayOf(
            R.drawable.menu_home_restaurant,
            R.drawable.menu_home_scan
        )

        product_category = arrayOf(
            "restaurant",
            "scan"
        )

        newArrayList = arrayListOf<CategoryProductsModel>()
        getUserData()
    }

    private fun getUserData() {
        for (i in img_category) {
            val a = CategoryProductsModel(img_category[i] , product_category[i])
            newArrayList.add(a)
        }
    }

    private fun initRecyclerView() {
        binding.categoryproductRecycler.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = CategoryProductAdapter(newArrayList)
        }
    }

    override fun initObserver() {
    }

    override fun initAction() {
    }

    override fun initData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryProductsBinding.inflate(inflater)
        return binding.root
    }

}
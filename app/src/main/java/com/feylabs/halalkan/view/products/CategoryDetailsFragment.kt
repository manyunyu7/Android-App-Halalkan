package com.feylabs.halalkan.view.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.*
import com.feylabs.halalkan.utils.base.BaseFragment
import com.mapbox.maps.extension.style.expressions.dsl.generated.product


class CategoryDetailsFragment : BaseFragment() {

    private lateinit var newArrayList: ArrayList<CategoryProductsModel>
//    private var _binding: FragmentListProductsBinding? = null

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!


    override fun initUI() {
        initDetailsProduct()
    }

    fun initDetailsProduct() {
//        binding.labelMasjidName.text = "asd"

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
        _binding = FragmentDetailProductBinding.inflate(inflater)
        return binding.root
    }

}
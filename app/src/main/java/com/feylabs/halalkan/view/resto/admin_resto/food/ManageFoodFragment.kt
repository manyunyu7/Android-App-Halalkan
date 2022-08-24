package com.feylabs.halalkan.view.resto.admin_resto.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.FoodCategoryResponse
import com.feylabs.halalkan.databinding.FragmentXrestoFoodBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.feylabs.halalkan.view.resto.detail.FoodCategoryAdapter
import com.feylabs.halalkan.view.resto.main.RestoFoodAdapter
import org.koin.android.viewmodel.ext.android.viewModel


class ManageFoodFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoFoodBinding? = null
    private val binding get() = _binding!!
    private val PERMISSION_CODE_STORAGE = 1001

    val viewModel by viewModel<AdminRestoViewModel>()

    private val foodAdapter by lazy { RestoFoodAdapter() }
    private val categoryAdapter by lazy { FoodCategoryAdapter() }


    private fun setupFoodAdapter() {
        binding.rvList.apply {
            adapter = foodAdapter
            setHasFixedSize(true)
            layoutManager = setLayoutManagerLinear()
        }

        binding.rvMenuCategory.apply {
            adapter = categoryAdapter
            layoutManager = setLayoutManagerHorizontal()
        }

        categoryAdapter.setupAdapterInterface(object : FoodCategoryAdapter.ItemInterface {
            override fun onclick(model: FoodCategoryResponse.FoodCategoryResponseItem) {
                //set current active menu
                viewModel.currentMenuTab.postValue(Pair(model.id, model.name))
            }
        })
    }

    private fun setAllMenu() = viewModel.currentMenuTab.postValue(Pair(-99, ""))
    private fun isAllMenu() = viewModel.currentMenuTab.value?.first == -99

    override fun initUI() {
        setupFoodAdapter()
    }

    override fun initObserver() {
        viewModel.foodByCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {
                    showCenterLoadingIndicator(false)
                }
                is Error -> {
                    showToast(it.message.toString())
                    showCenterLoadingIndicator(false)
                }
                is Loading -> {
                    showCenterLoadingIndicator(false)
                }
                is Success -> {
                    if (!isAllMenu().not()) {
                        it.data?.let {
                            foodAdapter.setWithNewData(it)
                            foodAdapter.notifyDataSetChanged()
                            checkFoodAdapter()
                        }
                    }
                }
            }
        }

        viewModel.foodCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data?.let {
                        setupFoodCategory(it)
                    }
                }
                is Loading -> {
                    showLoadingListFood(true)
                }
                else -> {
                    showLoadingListFood(false)
                }
            }
        }

        viewModel.restoFoodByCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {
                    showLoadingListFood(false)
                }
                is Error -> {
                    showToast(it.message.toString())
                    showLoadingListFood(false)
                }
                is Loading -> {
                    showLoadingListFood(true)
                }
                is Success -> {
                    showLoadingListFood(false)
                    it.data?.let {
                        foodAdapter.setWithNewData(it)
                        foodAdapter.notifyDataSetChanged()
                        checkFoodAdapter()
                    }
                }
            }

        }

        viewModel.currentMenuTab.observe(viewLifecycleOwner) {
            val foodCategoryId = it.first
            categoryAdapter.setActiveMenu(it.first)
            if (isAllMenu()) {
                viewModel.getAllFoodByResto(getRestoId())
            } else {
                viewModel.getRestoFoodByCategory(getRestoId(), foodCategoryId.toString())
            }
        }

        viewModel.allFoodLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Error -> {
                    showLoadingListFood(false)
                }
                is Loading -> {
                    showLoadingListFood(true)
                }
                is Success -> {
                    showLoadingListFood(false)
                    it.data?.let { data ->
                        if (isAllMenu()) {
                            foodAdapter.setWithNewData(data)
                            foodAdapter.notifyDataSetChanged()
                            checkFoodAdapter()
                        }
                    } ?: run {
                        //TODO
                    }
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
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.navigation_addEditFoodFragment)
        }
    }

    private fun getRestoId(): String {
        return getChoosenResto()
    }

    override fun initData() {
        viewModel.getDetailResto(getRestoId())
        viewModel.getFoodCategoryOnResto(getRestoId())
        viewModel.getAllFoodByResto(getRestoId())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoFoodBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkFoodAdapter() {
        if (foodAdapter.itemCount == 0) {
            binding.stateNoFood.makeVisible()
        } else {
            binding.stateNoFood.makeGone()
        }
    }

    private fun showCenterLoadingIndicator(value: Boolean) {
        if (value)
            binding.loadingCenterProgressBar.makeVisible()
        else
            binding.loadingCenterProgressBar.makeGone()
    }

    private fun setupFoodCategory(data: FoodCategoryResponse) {
        val tempData = data
        tempData.add(
            0, FoodCategoryResponse.FoodCategoryResponseItem(id = -99, "All")
        )
        categoryAdapter.setWithNewData(tempData)
        categoryAdapter.notifyDataSetChanged()
        setAllMenu()
    }

    private fun showLoadingListFood(value: Boolean) {
        if (value) {
            binding.rvList.makeGone()
            binding.loadingListFood.makeVisible()
        } else {
            binding.rvList.makeVisible()
            binding.loadingListFood.makeGone()
        }
    }



}
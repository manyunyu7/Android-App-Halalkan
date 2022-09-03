package com.feylabs.halalkan.view.resto.admin_resto.menu_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetEditFoodCategory
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetOrderNotes
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.FoodCategoryResponse
import com.feylabs.halalkan.databinding.FragmentXrestoCategoryBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class MenuCategoryFragment : BaseFragment() {


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
                showBottomSheetCategory(model.id.toString(), model.name.toString())
            }

            override fun onDelete(model: FoodCategoryResponse.FoodCategoryResponseItem) {
                DialogUtils.showConfirmationDialog(
                    context = requireContext(),
                    title = getString(R.string.label_are_you_sure),
                    message = getString(R.string.message_delete_food_category),
                    positiveAction = Pair("OK") {
                        viewModel.deleteFoodCategoryForResto(model.id.toString())
                    },
                    negativeAction = Pair(
                        getString(R.string.title_no),
                        { }),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            }

            override fun onEdit(model: FoodCategoryResponse.FoodCategoryResponseItem) {
                showBottomSheetCategory(model.id.toString(), model.name.toString())
            }
        })
    }

    private fun showBottomSheetCategory(currentId: String, currentName: String) {
        BottomSheetEditFoodCategory.instance(
            onDismiss = {
                viewModel.getFoodCategoryOnResto(getChoosenResto())
            },
            objectId = currentId,
            restoId = getChoosenResto(),
            currentName = currentName
        ).show(getMFragmentManager(), BottomSheetOrderNotes().tag)
    }

    override fun initObserver() {
        viewModel.editRestoFoodCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showLoading(false)
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.title_success),
                        message = getString(R.string.message_data_will_deleted),
                        positiveAction = Pair("OK") {
                            viewModel.getFoodCategoryOnResto(getChoosenResto())
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    viewModel.fireEditRestoFoodCategory()
                }
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    DialogUtils.showErrorDialog(
                        context = requireContext(),
                        title = getString(R.string.title_error),
                        message = it.message.toString(),
                        positiveAction = Pair("OK") {
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                    viewModel.fireEditRestoFoodCategory()
                }
                is Loading -> {
                    showLoading(true)
                }
            }
        }
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
        if (b) {
            binding.loadingAnim.makeVisible()
        } else {
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
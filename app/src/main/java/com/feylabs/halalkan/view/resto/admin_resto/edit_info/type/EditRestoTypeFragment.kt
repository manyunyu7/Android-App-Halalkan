package com.feylabs.halalkan.view.resto.admin_resto.edit_info.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.databinding.FragmentXrestoEditRestoTypeBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class EditRestoTypeFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoEditRestoTypeBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModel<AdminRestoViewModel>()

    private var mapRestoType = mutableMapOf<String, String>()

    override fun initUI() {
    }

    override fun initObserver() {
        viewModel.foodTypeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    val spinnerArray: MutableList<String> = mutableListOf()
                    it.data?.forEachIndexed { index, restaurantCertificationItem ->
                        spinnerArray.add(restaurantCertificationItem.name)
                        mapRestoType.put(
                            restaurantCertificationItem.name,
                            restaurantCertificationItem.id.toString()
                        )
                    }

                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        requireContext(), android.R.layout.simple_spinner_item, spinnerArray
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerCert.adapter = adapter
                }
            }
        }

        viewModel.updateRestoColumnLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = "Success",
                        message = "Data Updated Successfully",
                        positiveAction = Pair("OK") {
                          findNavController().popBackStack()
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }
        }

        viewModel.detailRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    it.data?.let {
                        setupUiFromNetwork(it)
                    }
                }
            }
        }
    }

    private fun showLoading(b: Boolean) {
        if(b){
            binding.includeLoading.root.makeVisible()
        }else{
            binding.includeLoading.root.makeGone()
        }
    }

    private fun setupUiFromNetwork(it: RestoDetailResponse) {
        binding.tvCurrentCert.text = it.data.detailResto.foodTypeName
    }

    override fun initAction() {
        binding.btnSave.setOnClickListener {
            val certification = mapRestoType[binding.spinnerCert.selectedItem]
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = "Are You Sure",
                message = "This action will updating your restaurant type",
                positiveAction = Pair("OK") {
                    viewModel.updateRestoColumn(getChoosenResto(),
                        updatepath = "resto-type",
                        columnValue = certification.toString(),name="type_food_id")
                },
                negativeAction = Pair(
                    "No",
                    { }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun initData() {
        viewModel.getFoodType()
        viewModel.getDetailResto(getChoosenResto())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoEditRestoTypeBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
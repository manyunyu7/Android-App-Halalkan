package com.feylabs.halalkan.view.resto.admin_resto.edit_info.certification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.databinding.FragmentXrestoEditCertificationBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class EditRestoCertificationFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoEditCertificationBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModel<AdminRestoViewModel>()

    private var mapCert = mutableMapOf<String, String>()

    override fun initUI() {
    }

    override fun initObserver() {
        viewModel.certLiveData.observe(viewLifecycleOwner) {
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
                        mapCert.put(
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
                        message = "Certification Data Updated Successfully",
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
        binding.tvCurrentCert.text = it.data.detailResto.certificationName
    }

    override fun initAction() {
        binding.btnSave.setOnClickListener {
            val certification = mapCert[binding.spinnerCert.selectedItem]
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = "Are You Sure",
                message = "This action will updating your certification",
                positiveAction = Pair("OK") {
                    viewModel.updateRestoColumn(getChoosenResto(),
                        updatepath = "cert",
                        name = "certification_id",
                        columnValue = certification.toString())
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
        viewModel.getDetailResto(getChoosenResto())
        viewModel.getRestoCert()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoEditCertificationBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
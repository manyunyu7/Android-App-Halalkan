package com.feylabs.halalkan.view.resto.admin_resto.edit_info.keramaian_lalin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.databinding.FragmentXrestoEditKeramaianLalinBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.google.android.gms.maps.GoogleMap
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class EditKeramaianLalinFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoEditKeramaianLalinBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModel<AdminRestoViewModel>()
    val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var mMap: GoogleMap

    override fun initUI() {
    }

    var targetLat = 0.0
    var targetLong = 0.0

    override fun initObserver() {
        viewModel.updateCommonLiveData.observe(viewLifecycleOwner) {
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
                        title = getString(R.string.title_success),
                        message = getString(R.string.message_data_updated_succesfully),
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
        if (b) {
            binding.includeLoading.root.makeVisible()
        } else {
            binding.includeLoading.root.makeGone()
        }
    }

    private fun setupUiFromNetwork(data: RestoDetailResponse) {
        val dataBangunan = data.data.detailResto.data_bangunan
        dataBangunan?.let {
            val jenisJalanRaw = it.jenisJalan
            if(jenisJalanRaw=="Aspal 1 Jalur"){
                binding.dropdownJenisJalan.setSelection(0)
            }
            if(jenisJalanRaw=="Aspal Lebih dari 1 Jalur"){
                binding.dropdownJenisJalan.setSelection(1)
            }
            if(jenisJalanRaw=="Non-Aspal"){
                binding.dropdownJenisJalan.setSelection(2)
            }

            binding.etLahanParkirMobil.editText?.setText(it.parkirMobil.orEmpty())
            binding.etLahanParkirMotor.editText?.setText(it.parkirMotor.orEmpty())
            binding.etMotor.editText?.setText(it.menitMotor.orEmpty())
            binding.etBisTruk.editText?.setText(it.menitTruk.orEmpty())
            binding.etMobil.editText?.setText(it.menitMobil.orEmpty())
        }

    }

    override fun initAction() {
        binding.btnSave.setOnClickListener {
            val etLahanParkirMobil = binding.etLahanParkirMobil.editText?.text.toString()
            val etLahanParkirMotor = binding.etLahanParkirMotor.editText?.text.toString()
            val etJmlMotor5Menit = binding.etMotor.editText?.text.toString()
            val etJmlMobil5Menit = binding.etMobil.editText?.text.toString()
            val etJmlBusTruk5Menit = binding.etBisTruk.editText?.text.toString()
            val inputJenisJalan = binding.dropdownJenisJalan.selectedItem.toString()

            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_resto_update_address),
                positiveAction = Pair("OK") {
                    viewModel.updateRestoLalinAndParkir(
                        idResto = getChoosenResto(),
                        fiveMinuteBusTruk = etJmlBusTruk5Menit,
                        fiveMinuteMobil = etJmlMobil5Menit,
                        fiveMinuteMotor = etJmlMotor5Menit,
                        parkirMobil = etLahanParkirMobil,
                        parkirMotor = etLahanParkirMotor,
                        jenisJalan = inputJenisJalan
                    )
                },
                negativeAction = Pair(
                    getString(R.string.title_no),
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentXrestoEditKeramaianLalinBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
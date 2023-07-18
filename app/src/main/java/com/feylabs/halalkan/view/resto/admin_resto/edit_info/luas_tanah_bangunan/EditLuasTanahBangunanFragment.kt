package com.feylabs.halalkan.view.resto.admin_resto.edit_info.luas_tanah_bangunan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.databinding.FragmentXrestoEditLuasTanahBangunanBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.google.android.gms.maps.GoogleMap
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class EditLuasTanahBangunanFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoEditLuasTanahBangunanBinding? = null
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
            val ijinDomisili = it.ijinDomisili
            if (ijinDomisili == "Ada") {
                binding.dropdownIzinDomisiliAvailable.setSelection(0)
            }
            if (ijinDomisili == "Pernah Ada") {
                binding.dropdownIzinDomisiliAvailable.setSelection(1)
            }
            if (ijinDomisili == "Belum Ada") {
                binding.dropdownIzinDomisiliAvailable.setSelection(2)
            }
            if (ijinDomisili == "Diurus Tidak Keluar") {
                binding.dropdownIzinDomisiliAvailable.setSelection(3)
            }

            val bisaDimajukan = it.bisaDimajukan
            if (bisaDimajukan == "Bisa") {
                binding.dropdownBisaDimajukan.setSelection(0)
            }
            if (bisaDimajukan == "Tidak Bisa") {
                binding.dropdownBisaDimajukan.setSelection(1)
            }


            binding.etLebarBangunan.editText?.setText(data.data.detailResto.data_bangunan?.lebarBangunan.orEmpty())
            binding.etPanjangBangunan.editText?.setText(data.data.detailResto.data_bangunan?.panjangBangunan.orEmpty())
            binding.etLebarTanah.editText?.setText(data.data.detailResto.data_bangunan?.lebarTanah.orEmpty())
            binding.etPanjangTanah.editText?.setText(data.data.detailResto.data_bangunan?.panjangTanah.orEmpty())
            binding.etJumlahLantai.editText?.setText(data.data.detailResto.data_bangunan?.jumlahLantai.orEmpty())
        }
    }

    override fun initAction() {

        binding.btnSave.setOnClickListener {
            val lt = binding.etLebarTanah.editText?.text.toString()
            val lb = binding.etLebarBangunan.editText?.text.toString()
            val pt = binding.etPanjangTanah.editText?.text ?: ""
            val pb = binding.etPanjangBangunan.editText?.text ?: ""
            val jumlahLantai = binding.etJumlahLantai.editText?.text ?: ""
            val inputBisaDimajukan = binding.dropdownBisaDimajukan.selectedItem.toString()
            val inputApakahIzinDomisiliAda = binding.dropdownIzinDomisiliAvailable.selectedItem.toString()

            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_resto_update_address),
                positiveAction = Pair("OK") {
                    viewModel.updateRestoLuasTanahBangunan(
                        idResto = getChoosenResto(),
                        lt = lt.toString(),
                        lb = lb.toString(),
                        pb = pb.toString(),
                        pt = pt.toString(),
                        jumlahLantai = jumlahLantai.toString(),
                        bisaDimajukan = inputBisaDimajukan,
                        ijinDomisiliAda = inputApakahIzinDomisiliAda
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
        _binding = FragmentXrestoEditLuasTanahBangunanBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
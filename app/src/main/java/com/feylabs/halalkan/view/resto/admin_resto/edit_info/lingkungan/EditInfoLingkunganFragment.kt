package com.feylabs.halalkan.view.resto.admin_resto.edit_info.lingkungan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource.Default
import com.feylabs.halalkan.data.remote.QumparanResource.Error
import com.feylabs.halalkan.data.remote.QumparanResource.Loading
import com.feylabs.halalkan.data.remote.QumparanResource.Success
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.databinding.FragmentXrestoEditKeadaanLingkunganBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.google.android.gms.maps.GoogleMap
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class EditInfoLingkunganFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoEditKeadaanLingkunganBinding? = null
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
            setDropdownSelection(binding.dropdownAlfamart, it.isAlfamart100Exist);
            setDropdownSelection(binding.dropdownIndomart, it.isIndomaret100Exist);
            setDropdownSelection(binding.dropdownSpbu, it.isSpbu100Exist);
            setDropdownSelection(binding.dropdownGereja, it.isGereja100Exist);
            setDropdownSelection(binding.dropdownMasjid, it.isMasjid100Exist);
            setDropdownSelection(binding.dropdownUniversitas, it.isUniv100Exist);
            setDropdownSelection(binding.dropdownUsahaLain, it.isCounterUsahaLain100Exist);
            setDropdownSelection(binding.dropdownSekolah, it.isSekolah100Exist);
            setDropdownSelection(binding.dropdownBengkel, it.isBengkel100Exist);
        }
    }

    private fun setDropdownSelection(dropdown: Spinner, value: String?) {
        when (value) {
            "Ada" -> {
                dropdown.setSelection(1)
            }
            "Tidak Ada" -> {
                dropdown.setSelection(2)
            }
            else -> {
                dropdown.setSelection(0)
            }
        }
    }


    override fun initAction() {

        binding.btnSave.setOnClickListener {
            val alfamart = binding.dropdownAlfamart.selectedItem?.toString().orEmpty();
            val indomaret = binding.dropdownIndomart.selectedItem?.toString().orEmpty();
            val counterUsahaLain = binding.dropdownUsahaLain.selectedItem?.toString().orEmpty();
            val gereja = binding.dropdownGereja.selectedItem?.toString().orEmpty();
            val masjid = binding.dropdownMasjid.selectedItem?.toString().orEmpty();
            val spbu = binding.dropdownSpbu.selectedItem?.toString().orEmpty();
            val univ = binding.dropdownUniversitas.selectedItem?.toString().orEmpty();
            val sekolah = binding.dropdownSekolah.selectedItem?.toString().orEmpty();
            val bengkel = binding.dropdownBengkel.selectedItem?.toString().orEmpty();
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_resto_update_address),
                positiveAction = Pair("OK") {
                    viewModel.updateKeadaanLingkungan(
                        idResto = getChoosenResto(),
                        alfamart = alfamart,
                        indomart = indomaret,
                        counterUsahaLain = counterUsahaLain,
                        gereja = gereja,
                        masjid = masjid,
                        spbu = spbu,
                        univ = univ,
                        sekolah = sekolah,
                        bengkel = bengkel
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
        _binding = FragmentXrestoEditKeadaanLingkunganBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
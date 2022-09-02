package com.feylabs.halalkan.view.resto.admin_resto.edit_info.operating_hours

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.data.remote.reqres.resto.operating_hour.RestoOperatingHourResponse
import com.feylabs.halalkan.databinding.FragmentXrestoEditOperatingHourBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.StringUtil.checkHourFormat
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.feylabs.halalkan.view.resto.order.detail.BottomSheetChooseDriverFragment
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class EditOperatingHoursFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoEditOperatingHourBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModel<AdminRestoViewModel>()

    private val mAdapter by lazy { OperatingHourAdapter() }

    var dayCode = -99

    override fun initUI() {
        binding.rv.apply {
            adapter = mAdapter
            layoutManager = setLayoutManagerLinear()
        }

        mAdapter.setupAdapterInterface(object : OperatingHourAdapter.ItemInterface {
            override fun onclick(model: RestoOperatingHourResponse.Data) {
                showBottomSheetEdit(model)
            }
        })
    }


    override fun initObserver() {
        viewModel.createEditRestoOperatingHourLiveData.observe(viewLifecycleOwner) {
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
                            viewModel.getRestoOperatingHour(getChoosenResto())
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }
        }
        viewModel.restoOperatingHourLiveData.observe(viewLifecycleOwner) {
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
                        mAdapter.setWithNewData(it.data.toMutableList())
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
    }

    override fun initAction() {
        binding.etEnd.editText?.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty()) {
                binding.etEnd.error = null
            }
        }

        binding.dropdownFruits.setItemClickListener { position, item ->
            dayCode = position
        }


        binding.etStart.editText?.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty()) {
                binding.etStart.error = null
            }
        }
        binding.btnSave.setOnClickListener {
            var isError = false
            val startHour = binding.etStart.editText?.text.toString()
            val endHour = binding.etEnd.editText?.text.toString()

            if (dayCode == -99) {
                isError = true
                showSnackbar(getString(R.string.message_please_choose_day), SnackbarType.ERROR)
            }
            if (startHour.checkHourFormat().not()) {
                isError = true
                binding.etStart.error = getString(R.string.message_error_time_format_24_hour)
                showSnackbar(
                    getString(R.string.message_error_time_format_24_hour),
                    SnackbarType.ERROR
                )
            }

            if (endHour.checkHourFormat().not()) {
                isError = true
                binding.etEnd.error = getString(R.string.message_error_time_format_24_hour)
                showSnackbar(
                    getString(R.string.message_error_time_format_24_hour),
                    SnackbarType.ERROR
                )
            }


            if (endHour.checkHourFormat() && startHour.checkHourFormat()) {
                if (compareTime(startHour, endHour)) {
                    isError = true
                    showSnackbar(
                        getString(R.string.message_time_close_must_greater_than_open),
                        SnackbarType.ERROR
                    )
                }
            } else {
                isError = true
            }

            if (isError.not()) {
                DialogUtils.showConfirmationDialog(
                    context = requireContext(),
                    title = getString(R.string.label_are_you_sure),
                    message = getString(R.string.message_data_will_uploaded),
                    positiveAction = Pair("OK") {
                        viewModel.createRestoOperatingHour(
                            restoId = getChoosenResto(),
                            start = startHour,
                            end = endHour,
                            dayCode = dayCode
                        )
                    },
                    negativeAction = Pair(
                        getString(R.string.title_no),
                        { }),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            }

        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showBottomSheetEdit(model: RestoOperatingHourResponse.Data) {
        val olz: (notes: String) -> Unit = { notes ->
            viewModel.getRestoOperatingHour(getChoosenResto())
        }

        EditOperatingHourBottomSheet.instance(
            dayCode = model.dayCode.toInt(),
            hourId = model.id,
            restoId = getChoosenResto(),
            selectedAction = olz,
            currentEnd = model.hourEnd,
            currentStart = model.hourStart
        ).show(getMFragmentManager(), BottomSheetChooseDriverFragment().tag)
    }


    private fun compareTime(start: String, end: String): Boolean {
        if (start.equals("00:00") && end.equals("00:00")) {
            return true
        }
        val hourFormat = SimpleDateFormat("HH:mm", Locale.KOREA)
        val startTime = hourFormat.parse(start)
        val endTime = hourFormat.parse(end)
        return !endTime.after(startTime)
    }

    override fun initData() {
        viewModel.getRestoOperatingHour(getChoosenResto())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoEditOperatingHourBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
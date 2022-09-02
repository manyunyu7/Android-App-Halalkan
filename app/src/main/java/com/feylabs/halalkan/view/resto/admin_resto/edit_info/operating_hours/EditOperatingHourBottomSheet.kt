package com.feylabs.halalkan.view.resto.admin_resto.edit_info.operating_hours

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.CommonUtil.makeVisible
import com.feylabs.halalkan.utils.CommonUtil.showSnackbar
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.StringUtil.checkHourFormat
import com.feylabs.halalkan.utils.base.BaseDialogFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.databinding.LayoutBottomsheetEditOperatingHourBinding as MainBinding
import com.feylabs.halalkan.view.resto.DriverViewModel
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class EditOperatingHourBottomSheet : BaseDialogFragment() {

    private lateinit var selectedAction: (notes: String) -> Unit

    private var currentStart = ""
    private var currentEnd = ""
    private var restoId = ""
    private var dayCode = -99
    private var hourId = -99

    val viewModel by viewModel<AdminRestoViewModel>()


    companion object {
        private val TAG = "BOTTOM_SHEET_NOTES_ACTION"
        fun instance(
            hourId: Int,
            restoId: String,
            currentStart: String,
            currentEnd: String,
            dayCode: Int,
            selectedAction: (notes: String) -> Unit,
        ): EditOperatingHourBottomSheet {
            EditOperatingHourBottomSheet().apply {
                this.hourId = hourId
                this.currentStart = currentStart
                this.currentEnd = currentEnd
                this.selectedAction = selectedAction
                this.dayCode = dayCode
                this.restoId = restoId
                return this
            }
        }
    }

    private lateinit var binding: MainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObserver()
    }

    private fun initObserver() {
        viewModel.createEditRestoOperatingHourLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {}
                is QumparanResource.Error -> {
                    showLoading(false)
                    DialogUtils.showErrorDialog(
                        context = requireContext(),
                        title = getString(R.string.title_error),
                        message = it.message.toString(),
                        positiveAction = Pair("OK") {
                            dismiss()
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.title_success),
                        message = getString(R.string.message_data_updated_succesfully),
                        positiveAction = Pair("OK") {
                            dismiss()
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
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

    private fun initUI() {
        val rootView = binding.root
        binding.etStart?.editText?.setText(this.currentStart)
        binding.etEnd?.editText?.setText(this.currentEnd)

        binding.btnDelete.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_data_will_deleted),
                positiveAction = Pair("OK") {
                    viewModel.deleteRestoOperatingHour(
                        hourId = hourId.toString(),
                        restoId = restoId,
                    )
                },
                negativeAction = Pair(
                    getString(R.string.title_no),
                    { }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }

        binding.btnSave.setOnClickListener {
            var isError = false
            val startHour = binding.etStart.editText?.text.toString()
            val endHour = binding.etEnd.editText?.text.toString()


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
                        viewModel.updateRestoOperatingHour(
                            hourId = hourId.toString(),
                            restoId = restoId,
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

    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        selectedAction.invoke("")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }


}

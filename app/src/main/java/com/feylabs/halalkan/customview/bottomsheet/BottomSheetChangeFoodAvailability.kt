package com.feylabs.halalkan.customview.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.databinding.LayoutBottomsheetChangeFoodAvailabilityBinding as MainBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseDialogFragment
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class BottomSheetChangeFoodAvailability : BaseDialogFragment() {


    private var objectId = ""
    private var newStatus = -99
    private var oldStatus = 0
    private var onDismiss: (() -> Unit)? = null
    val viewModel by viewModel<AdminRestoViewModel>()


    companion object {
        private val TAG = "BOTTOM_SHEET_NOTES_CHANGE_AVAILABILITY"
        fun instance(
            objectId: String,
            oldStatus: Int,
            onDismiss: () -> Unit,
        ): BottomSheetChangeFoodAvailability {
            BottomSheetChangeFoodAvailability().apply {
                this.objectId = objectId
                this.oldStatus = oldStatus
                this.onDismiss = onDismiss
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
        viewModel.editFoodAvailability.observe(viewLifecycleOwner) {
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }

    private fun initUI() {
        val rootView = binding.root

        binding.dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                newStatus = position

                if (position==0){
                    newStatus=-99
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                newStatus = -99
            }
        }


        binding.btnSave.setOnClickListener {
            var isError = false

            if (newStatus == -99) {
                isError=true
                DialogUtils.showErrorDialog(
                    context = requireContext(),
                    title = getString(R.string.title_error),
                    message = getString(R.string.message_please_choose_avail_status_first),
                    positiveAction = Pair("OK") {
                    },
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            }

            if (isError.not()) {
                DialogUtils.showConfirmationDialog(
                    context = requireContext(),
                    title = getString(R.string.label_are_you_sure),
                    message = getString(R.string.message_data_will_updated),
                    positiveAction = Pair("OK") {
                        viewModel.editFoodAvailability(objectId, isAvailable = newStatus)
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }


}

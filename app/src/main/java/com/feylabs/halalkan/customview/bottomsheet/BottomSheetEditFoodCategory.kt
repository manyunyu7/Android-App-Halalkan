package com.feylabs.halalkan.customview.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.databinding.LayoutBottomsheetEditFoodCategoryBinding  as MainBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseDialogFragment
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class BottomSheetEditFoodCategory : BaseDialogFragment() {


    private var objectId = ""
    private var restoId = ""
    private var currentName = ""
    private var onDismiss : (() -> Unit)? = null

    val viewModel by viewModel<AdminRestoViewModel>()


    companion object {
        private val TAG = "BOTTOM_SHEET_NOTES_ACTION"
        fun instance(
            objectId: String,
            restoId: String,
            currentName: String,
            onDismiss: () -> Unit,
        ): BottomSheetEditFoodCategory {
            BottomSheetEditFoodCategory().apply {
                this.objectId = objectId
                this.currentName = currentName
                this.restoId = restoId
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
        viewModel.editRestoFoodCategoryLiveData.observe(viewLifecycleOwner) {
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

        binding.etNotes?.editText?.setText(currentName)

        binding.btnSave.setOnClickListener {
            var isError = false
            val name = binding.etNotes.editText?.text.toString()

            if (isError.not()) {
                DialogUtils.showConfirmationDialog(
                    context = requireContext(),
                    title = getString(R.string.label_are_you_sure),
                    message = getString(R.string.message_data_will_uploaded),
                    positiveAction = Pair("OK") {
                        viewModel.editFoodCategoryForResto(
                            name = name,
                            categoryId = objectId,
                            restoId = restoId
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
        onDismiss?.invoke()
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

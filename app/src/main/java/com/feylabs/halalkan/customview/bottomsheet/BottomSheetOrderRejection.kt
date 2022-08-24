package com.feylabs.halalkan.customview.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.LayoutBottomsheetRejectOrderBinding as MainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.scope.currentScope

class BottomSheetOrderRejection : BottomSheetDialogFragment() {

    private lateinit var selectedAction: (notes: String) -> Unit

    private var menuId: String = ""
    private var existingNotes: String = ""
    private var type = ""

    companion object {
        private val TAG = "BOTTOM_SHEET_ORDER_REJECT"
        fun instance(
            objectId: String,
            selectedAction: (notes: String) -> Unit,
            type: String = "",
            existingNotes: String = "",
        ): BottomSheetOrderRejection {
            BottomSheetOrderRejection().apply {
                this.selectedAction = selectedAction
                this.menuId = objectId
                this.type = type
                this.existingNotes = existingNotes
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
    }

    private fun initUI() {
        binding.btnSave.setOnClickListener {
            val notes = binding.etNotes.editText?.text.toString()
            selectedAction.invoke(notes)
            dismiss()
        }
        binding.etNotes.editText?.setText(existingNotes)

        binding.btnClear.setOnClickListener {
            val notes = ""
            binding.etNotes?.editText?.setText("")
        }
    }



    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }


}

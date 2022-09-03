package com.feylabs.halalkan.customview.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.LayoutBottomsheetFoodActionBinding  as MainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetChooseFoodAction : BottomSheetDialogFragment() {

    private lateinit var selectedAction: (selectedId: String, action: MyBottomSheetAction) -> Unit

    private var title: String = "Action"
    private var description: String = ""
    private var objectId: String = ""
    private var ownerId: String = ""

    private var type = ""

    lateinit private var bsGeneralActionInterface: BottomSheetGeneralInterface

    companion object {
        private val TAG = "BOTTOM_SHEET_CHOOSE_FOOD_ACTION"
        fun instance(
            description: String,
            title: String,
            objectId: String,
            ownerId: String,
            selectedAction: (selectedId: String, action: MyBottomSheetAction) -> Unit,
            type: String = "",
        ): BottomSheetChooseFoodAction {
            BottomSheetChooseFoodAction().apply {
                this.description = description
                this.title = title
                this.selectedAction = selectedAction
                this.objectId = objectId
                this.ownerId = ownerId
                this.type = type
                return this
            }
        }
    }

    private lateinit var binding: MainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::bsGeneralActionInterface.isInitialized) {
            bsGeneralActionInterface.getBinding(binding)
        }
        initUI()
    }

    private fun initUI() {

        binding.containerEdit.setOnClickListener {
            dismiss()
            selectedAction.invoke(objectId, MyBottomSheetAction.EDIT)
        }

        binding.containerSee.setOnClickListener {
            dismiss()
            selectedAction.invoke(objectId, MyBottomSheetAction.SEE)
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    interface BottomSheetGeneralInterface {
        fun getBinding(binding: MainBinding)
    }
}


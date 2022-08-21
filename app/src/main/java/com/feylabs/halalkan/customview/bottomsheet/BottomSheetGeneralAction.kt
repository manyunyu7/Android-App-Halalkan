package com.feylabs.halalkan.customview.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.CommonUtil.makeVisible
import com.feylabs.halalkan.databinding.LayoutBottomsheetGeneralBinding as MainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetGeneralAction : BottomSheetDialogFragment() {

    private lateinit var selectedAction: (selectedId: String, action: MyBottomSheetAction) -> Unit

    private var title: String = "Action"
    private var description: String = ""
    private var objectId: String = ""
    private var ownerId: String = ""

    private var type = ""

    lateinit private var bsGeneralActionInterface: BottomSheetGeneralInterface

    companion object {
        private val TAG = "BOTTOM_SHEET_GENERAL_ACTION"
        fun instance(
            description: String,
            title: String,
            objectId: String,
            ownerId: String,
            selectedAction: (selectedId: String, action: MyBottomSheetAction) -> Unit,
            type : String = "",
            ): BottomSheetGeneralAction {
            BottomSheetGeneralAction().apply {
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

        binding.containerDelete.setOnClickListener {
            dismiss()
            selectedAction.invoke(objectId, MyBottomSheetAction.DELETE)
        }

        binding.containerEdit.setOnClickListener {
            dismiss()
            selectedAction.invoke(objectId, MyBottomSheetAction.EDIT)
        }

        binding.containerSee.setOnClickListener {
            dismiss()
            selectedAction.invoke(objectId, MyBottomSheetAction.SEE)
        }

        val role = MyPreference(requireContext()).getUserRole().orEmpty()

        if (MyPreference(requireContext()).isLoggedIn()) {
            //if not admin
            if (role != "1") {
                //if eligible user
                if (ownerId == MyPreference(requireContext()).getUserID()){
                    initUiEligible()
                }else{
                    initUiUnauthorized()
                }
            }
        } else {
            initUiUnauthorized()
        }

        binding.tvTitleFilter.text = description


        if (this.type=="comment"){
            binding.containerEdit.makeGone()
            binding.containerSee.makeGone()
        }

    }

    private fun initUiUnauthorized() {
        binding.containerEdit.makeGone()
        binding.containerSee.makeVisible()
        binding.containerDelete.makeGone()
    }

    private fun initUiEligible() {
        binding.containerEdit.makeVisible()
        binding.containerSee.makeVisible()
        binding.containerDelete.makeVisible()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    interface BottomSheetGeneralInterface {
        fun getBinding(binding: com.feylabs.halalkan.databinding.LayoutBottomsheetGeneralBinding)
    }
}

enum class MyBottomSheetAction {
    EDIT, CREATE, DELETE, SEE, OTHER
}
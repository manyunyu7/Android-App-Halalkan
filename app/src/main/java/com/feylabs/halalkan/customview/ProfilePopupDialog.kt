package com.feylabs.halalkan.customview

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomViewLayoutPopupProfileMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class UiKitContentProfile : BottomSheetDialogFragment() {


    private var userName: String = ""
    private var groupMemberName: String = ""
    private var position: String = ""
    private var email: String = ""
    private var phone: String = ""
    private var gender: String = ""
    private var image: String = ""

    private lateinit var phoneNumberClickListener : () -> Unit
    private lateinit var emailClickListener : () -> Unit

    companion object {
        val TAG = "PROFILE_POPUP_DIALOG"
        fun instance(
            userName: String,
            groupMemberName: String,
            position: String,
            email: String,
            phone: String,
            gender: String,
            phoneNumberListener : () -> Unit,
            emailListener : () -> Unit,
        ): UiKitContentProfile {
            UiKitContentProfile().apply {
                this.gender = gender
                this.phone = phone
                this.email = email
                this.groupMemberName = groupMemberName
                this.userName = userName
                this.position = position
                this.emailClickListener = emailListener
                this.phoneNumberClickListener = phoneNumberListener
                return this
            }
        }
    }

    private lateinit var binding: CustomViewLayoutPopupProfileMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CustomViewLayoutPopupProfileMainBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    private fun initUI() = with(binding) {
        this@UiKitContentProfile.apply {
            tvName.text = userName
            tvPosition.text = position
            includeContent.apply {
                labelDisplayEmail.text = email
                labelDisplayGender.text = gender
                labelDisplayPhone.text = phone
                labelDisplayGroupMember.text = groupMemberName
                labelDisplayPhone.setOnClickListener {
                    this@UiKitContentProfile.phoneNumberClickListener.invoke()
                }
                labelDisplayEmail.setOnClickListener {
                    this@UiKitContentProfile.emailClickListener.invoke()
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPhoneNumber(text: String) {
        this.phone = text
    }


}
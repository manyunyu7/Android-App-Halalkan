package com.feylabs.halalkan.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.LayoutDialogConfBinding
import com.feylabs.halalkan.databinding.LayoutDialogSingleButtonImageBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImage

object DialogUtils {
    private lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null


    fun showConfirmationDialog(
        context: Context,
        title: String = "",
        message: String = "",
        messageSpannable: SpannableStringBuilder? = null,
        negativeAction: Pair<String, (() -> Unit)?>? = null,
        positiveAction: Pair<String, (() -> Unit)?>,
        autoDismiss: Boolean = false,
        buttonAllCaps: Boolean = true,
        img: Int = R.drawable.ic_checklist
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_dialog_conf, null as ViewGroup?, false)
        val binding = LayoutDialogConfBinding.bind(view)
        binding.tvTitle.text = title
        if (messageSpannable != null)
            binding.tvMessage.text = messageSpannable
        else
            binding.tvMessage.text = message
        binding.btnPositive.let {
//            it.text = positiveAction.first
            it.setOnClickListener {
                dismissDialog()
                positiveAction.second?.invoke()
            }
            it.isAllCaps = buttonAllCaps
        }

        negativeAction?.let { pair ->
            binding.btnNegative.let {
                it.visibility = View.VISIBLE
//                it.text = pair.first
                it.setOnClickListener {
                    dismissDialog()
                    pair.second?.invoke()
                }
                it.isAllCaps = buttonAllCaps
            }
        }

        builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(autoDismiss)

        if (dialog == null) {
            dialog = builder.create()
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
        }
    }


    private fun dismissDialog() {
        dialog?.dismiss()
        dialog = null
    }

    fun showSuccessDialog(
        context: Context,
        title: String,
        message: String,
        positiveAction: Pair<String, (() -> Unit)?>,
        autoDismiss: Boolean = false,
        buttonAllCaps: Boolean = true,
        img: Int = R.drawable.ic_checklist
    ) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_dialog_single_button_image, null as ViewGroup?, false)
        val binding = LayoutDialogSingleButtonImageBinding.bind(view)
        binding.tvTitle.text = title
        binding.tvMessage.text = message
        binding.btnPositive.let {
            it.text = positiveAction.first
            it.setOnClickListener {
                dismissDialog()
                positiveAction.second?.invoke()
            }
            it.isAllCaps = buttonAllCaps
        }
        binding.imgLogo.loadImage(context, img)
        builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(autoDismiss)

        if (dialog == null) {
            dialog = builder.create()
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
        }
    }


}
package com.feylabs.halalkan.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomViewSearchLanguageDialogBinding
import com.feylabs.halalkan.customview.searchwithimage.ListSearchWithImageAdapter
import com.feylabs.halalkan.databinding.CustomViewItemAskBlockedPermissionBinding
import com.feylabs.halalkan.utils.Network

class AskBlockedPermissionUI : FrameLayout {

    private var text = ""
    private var positiveAction = {}
    private var negativeAction = {}
    private var imageDrawable = R.drawable.ic_microphone_permi

    val adapter by lazy { ListSearchWithImageAdapter() }

    private var binding: CustomViewItemAskBlockedPermissionBinding
    private var parentView: ViewGroup? = null

    init { // inflate binding and add as view
        binding = CustomViewItemAskBlockedPermissionBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) :
            super(context, attributeSet, defStyle) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    class Builder(
        val context: Context,
        var text: String? = null,
        var positiveAction: () -> Unit? = {},
        var negativeAction: () -> Unit? = {},
        var imageDrawable: Int = R.drawable.ic_microphone_permi,
    ) {
        fun text(mtext: String) = apply {
            this.text = mtext
        }

        fun positiveAction(positiveAction: () -> Unit?) = apply {
            this.positiveAction = positiveAction
        }

        fun negativeAction(negativeAction: () -> Unit?) = apply {
            this.negativeAction = negativeAction
        }

        fun image(imageDrawable: Int) {
            this.imageDrawable = imageDrawable
        }

        fun build() = AskBlockedPermissionUI(context)

    }

    fun initView() {
        binding.rootView.setOnClickListener {
            parentView?.removeView(binding.root) ?: run {
                binding.root.visibility = View.GONE
            }
        }

        binding.btnPositive.setOnClickListener {
            positiveAction.invoke()
        }

        binding.btnNegative.setOnClickListener {
            negativeAction.invoke()
        }

        binding.text.text = text

        Glide.with(context)
            .load(imageDrawable)
            .skipMemoryCache(true)
            .into(binding.image)
    }

    fun setDescriptionText(newText: String) {
        this.text = newText
        initView()
    }

    fun setPositiveAction(action: () -> Unit) {
        this.positiveAction = action
        initView()
    }

    fun setNegativeAction(action: () -> Unit) {
        this.negativeAction = action
        initView()
    }

    fun show(parentView: ViewGroup) {
        binding = CustomViewItemAskBlockedPermissionBinding.inflate(
            LayoutInflater.from(context),
            parentView,
            false
        )
        binding.root.visibility = View.VISIBLE

        binding.rootView.animation = AnimationUtils.loadAnimation(
            context,
            R.anim.item_animation_falldown
        )
        initView()
        parentView.addView(binding.root)
    }

}
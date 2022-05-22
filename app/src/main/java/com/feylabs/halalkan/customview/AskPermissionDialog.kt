package com.feylabs.halalkan.customview

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.searchwithimage.ListSearchWithImageAdapter
import com.feylabs.halalkan.databinding.CustomViewItemAskBlockedPermissionBinding

class AskPermissionDialog : FrameLayout {

    class Builder(
        val context: Context,
        var text: String? = null,
        var positiveAction: (() -> Unit)? = null,
        var negativeAction: (() -> Unit)? = null,
        var imageDrawable: Int = R.drawable.ic_microphone_permi,
    ) {
        fun text(mtext: String) = apply {
            this.text = mtext
        }

        fun positiveAction(positiveAction: () -> Unit) = apply {
            this.positiveAction = positiveAction
        }

        fun negativeAction(negativeAction: () -> Unit) = apply {
            this.negativeAction = negativeAction
        }

        fun image(imageDrawable: Int) {
            this.imageDrawable = imageDrawable
        }

        fun build() = AskPermissionDialog(this, context)

    }

    private lateinit var mActivity : Activity
    private var text = ""
    private var positiveAction: (() -> Unit)? = null
    private var negativeAction: (() -> Unit)? = null
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

    constructor(builder: Builder, context: Context) : super(context) {
        this.positiveAction = builder.positiveAction
        this.negativeAction = builder.negativeAction
        this.text = builder.text.orEmpty()
        this.imageDrawable = builder.imageDrawable

        setDescriptionText(builder.text.orEmpty())
        setPositiveAction(builder.positiveAction)
        setNegativeAction(builder.positiveAction)
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) :
            super(context, attributeSet, defStyle) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    fun initView() {
        binding.rootView.setOnClickListener {
            parentView?.removeView(binding.root) ?: run {
                binding.root.visibility = View.GONE
            }
        }

        binding.btnPositive.setOnClickListener {
            positiveAction?.let {
                it.invoke()
                this.dismiss()
            }
        }

        binding.btnNegative.setOnClickListener {
            negativeAction?.let {
                it.invoke()
                this.dismiss()
            }
        }

        if (text.isNotEmpty()) {
            binding.text.text = text
        } else {
            binding.text.text = "Aplikasi Membutuhkan Izin Anda Untuk Menggunakan GPS Perangkat"
        }

        Glide.with(context)
            .load(imageDrawable)
            .skipMemoryCache(true)
            .into(binding.image)
    }

    fun setDescriptionText(newText: String) {
        this.text = newText
        initView()
    }

    fun setPositiveAction(action: (() -> Unit)?) {
        this.positiveAction = action
        initView()
    }

    fun setNegativeAction(action: (() -> Unit)?) {
        this.negativeAction = action
        initView()
    }

    private fun requireActivity(): Activity {
        return mActivity
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

    fun dismiss(parentView: ViewGroup? = null) {
        if (parentView != null) {
            parentView.removeView(binding.rootView)
        } else {
            binding.rootView.visibility = View.GONE
        }
    }

}
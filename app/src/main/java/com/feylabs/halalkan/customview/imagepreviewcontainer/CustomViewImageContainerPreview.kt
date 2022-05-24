package com.feylabs.halalkan.customview.imagepreviewcontainer

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomStatusBinding
import com.feylabs.halalkan.databinding.CustomviewImageContainerPreviewBinding

class CustomViewImageContainerPreview : FrameLayout {

    private var title: String = ""

    private lateinit var binding: CustomviewImageContainerPreviewBinding

    init { // inflate binding and add as view
        binding = CustomviewImageContainerPreviewBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        extractAttributes(attributeSet)
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        extractAttributes(attributeSet)
        initView(context)
    }

    private fun initView(context: Context?) {
        title(title)
    }

    fun build(
        text: String
    ) {
        binding.tvStatus.text=text
    }

    fun title(title: String) {
        this.title = title
        binding.tvStatus.text = title
    }


    private fun extractAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomStatus)
        title = typedArray.getString(R.styleable.CustomStatus_CustomTitle) ?: title
        typedArray.recycle()
    }

}
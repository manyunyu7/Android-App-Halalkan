package com.feylabs.halalkan.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.feylabs.halalkan.databinding.CustomviewMuskoMenuItemBinding

class MuskoProfileMenuItem : FrameLayout {

    private var title: String = ""

    private var binding: CustomviewMuskoMenuItemBinding = CustomviewMuskoMenuItemBinding.inflate(LayoutInflater.from(context))

    init { // inflate binding and add as view
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

    }

    fun build(
        text: String,
        logo: Drawable
    ) {
        binding.ivMenuLogo.setImageDrawable(logo)
        binding.tvMenuName.text = text
    }


    private fun extractAttributes(attrs: AttributeSet) {

    }

}
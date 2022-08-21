package com.feylabs.halalkan.customview

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomStatusBinding
import com.feylabs.halalkan.databinding.CustomviewStarIndicatorBinding
import com.feylabs.halalkan.databinding.RazCustomInfoPlusMinusBinding

class CustomPlusMinus : FrameLayout {

    private var quantity: Int = 0

    private var binding: RazCustomInfoPlusMinusBinding

    lateinit var customPlusMinusInterface: OnQuantityChangeListener

    init { // inflate binding and add as view
        binding = RazCustomInfoPlusMinusBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        initView(context)
    }

    private fun initView(context: Context?) {
        binding.tvQuantity.text = this.quantity.toString()
        binding.btnPlus.setOnClickListener {
            addOne()
        }

        binding.btnMinus.setOnClickListener {
            removeOne()
        }
    }

    fun setQuantity(quantity: Int) {
        this.quantity = quantity
        initView(context)
    }

    fun addOne() {
        this.quantity += 1
        binding.tvQuantity.text = this.quantity.toString()

        if (::customPlusMinusInterface.isInitialized) {
            customPlusMinusInterface.onChange(this.quantity)
        }

    }

    fun removeOne() {
        if (quantity > 0) {
            this.quantity -= 1
        }
        binding.tvQuantity.text = this.quantity.toString()
        if (::customPlusMinusInterface.isInitialized) {
            customPlusMinusInterface.onChange(this.quantity)
        }
    }

    interface OnQuantityChangeListener {
        fun onChange(quantity: Int)
    }


}
package com.feylabs.halalkan.customview

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomviewStarIndicatorBinding
import com.feylabs.halalkan.databinding.CustomviewStarStatsBinding

class CustomStarStatsReview : FrameLayout {

    private var starCount : Double = 0.0

    private var binding: CustomviewStarStatsBinding

    init { // inflate binding and add as view
        binding =  CustomviewStarStatsBinding.inflate(LayoutInflater.from(context))
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
        starCount(starCount)
    }

    fun starCount(count:Double){
        this.starCount=count
        setupStarUi(count)
    }

    private fun setupStarUi(count: Double) {

    }


    private fun extractAttributes(attrs: AttributeSet) {

    }

}
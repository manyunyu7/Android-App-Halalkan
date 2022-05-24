package com.feylabs.halalkan.customview

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomviewStarIndicatorBinding

class CustomStarIndicatorReview : FrameLayout {

    private var starCount : Double = 0.0

    private lateinit var binding: CustomviewStarIndicatorBinding

    init { // inflate binding and add as view
        binding = CustomviewStarIndicatorBinding.inflate(LayoutInflater.from(context))
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
        when{
            count>0.0 && count<=1.0->{
                binding.star1.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star2.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star3.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star4.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star5.setBackgroundResource(R.drawable.ic_star_grey)
            }
            count>1.0 && count<=1.0->{
                binding.star1.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star2.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star3.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star4.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star5.setBackgroundResource(R.drawable.ic_star_grey)
            }
            count>1.0 && count<=2.0->{
                binding.star1.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star2.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star3.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star4.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star5.setBackgroundResource(R.drawable.ic_star_grey)
            }
            count>2.0 && count<=3.0->{
                binding.star1.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star2.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star5.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star5.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star5.setBackgroundResource(R.drawable.ic_star_grey)
            }
            count>3.0 && count<=4.0->{
                binding.star1.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star2.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star3.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star5.setBackgroundResource(R.drawable.ic_star_grey)
                binding.star5.setBackgroundResource(R.drawable.ic_star_grey)
            }
            count>4.0 && count<=4.5->{
                binding.star1.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star2.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star3.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star4.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star5.setBackgroundResource(R.drawable.ic_star_grey)
            }
            count>4.5 ->{
                binding.star1.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star2.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star3.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star4.setBackgroundResource(R.drawable.ic_star_gold)
                binding.star5.setBackgroundResource(R.drawable.ic_star_gold)
            }
        }

    }


    private fun extractAttributes(attrs: AttributeSet) {

    }

}
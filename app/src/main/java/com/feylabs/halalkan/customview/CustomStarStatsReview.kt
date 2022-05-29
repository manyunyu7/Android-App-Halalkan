package com.feylabs.halalkan.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewsResponse
import com.feylabs.halalkan.databinding.CustomviewStarStatsBinding
import com.taufiqrahman.reviewratings.BarLabels
import com.taufiqrahman.reviewratings.RatingReviews
import java.util.*


class CustomStarStatsReview : FrameLayout {

    private var starCount: Double = 0.0

    private var binding: CustomviewStarStatsBinding

    init { // inflate binding and add as view
        binding = CustomviewStarStatsBinding.inflate(LayoutInflater.from(context))
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
       setupStarUi()
    }

    fun setupStarUi(reviews: MasjidReviewsResponse.ReviewCount? = null) {
        val ratingReviews = binding.ratingsReviews

        val colors = intArrayOf(
            Color.parseColor("#0e9d58"),
            Color.parseColor("#bfd047"),
            Color.parseColor("#ffc105"),
            Color.parseColor("#ef7e14"),
            Color.parseColor("#d36259")
        )

        var raters = intArrayOf(
            Random().nextInt(100),
            Random().nextInt(100),
            Random().nextInt(100),
            Random().nextInt(100),
            Random().nextInt(100)
        )

        if(reviews!=null){
            raters = intArrayOf(
                reviews.rating1,
                reviews.rating2,
                reviews.rating3,
                reviews.rating4,
                reviews.rating5,
            )
        }

        ratingReviews.createRatingBars(100, BarLabels.STYPE1, colors, raters)
    }


    private fun extractAttributes(attrs: AttributeSet) {

    }

}
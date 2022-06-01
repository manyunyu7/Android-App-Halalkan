package com.feylabs.halalkan.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.databinding.CustomviewStarStatsBinding
import com.feylabs.halalkan.utils.NumberUtil.Companion.round
import com.taufiqrahman.reviewratings.BarLabels
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
        setStartBarUi()
        setStarCount(0.0)
    }

    @SuppressLint("SetTextI18n")
    fun setStarCount(starCount: Double) {
        binding.starOnStats.setupStarUi(starCount)
        binding.tvStar.text = starCount.round(2).toString()
    }

    @SuppressLint("SetTextI18n")
    fun setStartBarUi(reviews: MasjidReviewPaginationResponse.ReviewCount? = null) {
        val ratingReviews = binding.ratingsReviews

        reviews?.let {
            binding.descStarCount.text =
                (it.rating1 + it.rating2 + it.rating3 + it.rating4 + it.rating5).toString() + " Rating"
        }

        val colors = intArrayOf(
            Color.parseColor("#0e9d58"),
            Color.parseColor("#bfd047"),
            Color.parseColor("#ffc105"),
            Color.parseColor("#ef7e14"),
            Color.parseColor("#d36259")
        )

        var raters = intArrayOf(0, 0, 0, 0, 0)

        if (reviews != null) {
            raters = intArrayOf(
                reviews.rating5,
                reviews.rating4,
                reviews.rating3,
                reviews.rating2,
                reviews.rating1,
            )

            val maxValues = raters.maxOrNull() ?: 100
            ratingReviews.createRatingBars(maxValues, BarLabels.STYPE1, colors, raters)
        } else {
            ratingReviews.createRatingBars(100, BarLabels.STYPE4, colors, raters)
        }
    }


    private fun extractAttributes(attrs: AttributeSet) {

    }

}
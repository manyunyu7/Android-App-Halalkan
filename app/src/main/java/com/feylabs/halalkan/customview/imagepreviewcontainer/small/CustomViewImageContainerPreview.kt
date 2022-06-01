package com.feylabs.halalkan.customview.imagepreviewcontainer.small

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewImageContainerAdapter
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewImageContainerAdapterSmall
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.databinding.CustomviewImageContainerPreviewBinding

@SuppressLint("NotifyDataSetChanged")
class CustomViewImageContainerPreviewSmall : FrameLayout {

    private var title: String = ""

    private var binding: CustomviewImageContainerPreviewBinding

    private val adapter by lazy { CustomViewImageContainerAdapterSmall() }

    init { // inflate binding and add as view
        binding = CustomviewImageContainerPreviewBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
        setupAdapter()
        setupRecyclerview()
    }

    private fun setupRecyclerview() {
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupAdapter() {
        adapter.setupAdapterInterface(object : CustomViewImageContainerAdapterSmall.ItemInterface {
            override fun onclick(model: CustomViewPhotoModel) {

            }
        })
    }

    fun setLoading(status: Boolean, isEmpty: Boolean = false) {
        if (status) {
            binding.rv.visibility = View.GONE
            binding.loading.root.visibility = View.VISIBLE
        } else {
            binding.rv.visibility = View.VISIBLE
            binding.loading.root.visibility = View.GONE
        }
    }

    /**
     * Add all image, and replace image before to adapter
     */
    fun replaceAllImage(newData: MutableList<CustomViewPhotoModel>) {
        adapter.setWithNewData(newData)
        adapter.notifyDataSetChanged()
    }

    /**
     * Add new image
     */
    fun addNewImage(newData: CustomViewPhotoModel) {
        adapter.addNewData(newData)
        adapter.notifyDataSetChanged()
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


    private fun extractAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomStatus)
        title = typedArray.getString(R.styleable.CustomStatus_CustomTitle) ?: title
        typedArray.recycle()
    }

}
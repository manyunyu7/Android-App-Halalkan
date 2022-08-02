package com.feylabs.halalkan.customview.menu_tab

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomViewSearchLanguageDialogBinding
import com.feylabs.halalkan.customview.searchwithimage.ListSearchWithImageAdapter
import com.feylabs.halalkan.databinding.CustomViewMenuTabBinding

class CustomMenuTab : FrameLayout {

    val adapter by lazy { MenuTabAdapter() }

    private lateinit var binding: CustomViewMenuTabBinding
    private var parentView: ViewGroup? = null

    init { // inflate binding and add as view
        binding = CustomViewMenuTabBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) :
            super(context, attributeSet, defStyle) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(context)
    }

    fun addMenuItem(model: MenuTabModel){
        adapter.addData(model)
    }

    fun initView(context: Context) {
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.setHasFixedSize(true)

        adapter.setupAdapterInterface(object :MenuTabAdapter.ItemInterface{
            override fun onclick(model: MenuTabModel) {
                adapter
            }

        })

    }


    fun show(parentView: ViewGroup) {
        binding = CustomViewMenuTabBinding.inflate(
            LayoutInflater.from(context),
            parentView,
            false
        )
        binding.root.visibility = View.VISIBLE

        initView(context)
        parentView.addView(binding.root)
    }

}
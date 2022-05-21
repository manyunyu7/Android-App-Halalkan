package com.feylabs.halalkan.customview

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
import com.feylabs.halalkan.view.utilview.searchwithimage.ListSearchWithImageAdapter

class SearchLanguageDialog : FrameLayout {

    val adapter by lazy { ListSearchWithImageAdapter() }

    private lateinit var binding: CustomViewSearchLanguageDialogBinding
    private var parentView: ViewGroup? = null

    init { // inflate binding and add as view
        binding = CustomViewSearchLanguageDialogBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }

    companion object {
        const val SEARCH_LANG_DIALOG_TAG = "dialogSeafaca"
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

    lateinit var searchLangDialog: SearchLangDialogFragment

    fun initView(context: Context) {
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.setHasFixedSize(true)

        binding.rootview.setOnClickListener {
            parentView?.removeView(binding.root) ?: run {
                binding.root.visibility = View.GONE
            }
        }
    }

    fun renameTitle(text: String) {
        binding.txtDia.text = text
    }

    fun show(parentView: ViewGroup) {
        binding = CustomViewSearchLanguageDialogBinding.inflate(
            LayoutInflater.from(context),
            parentView,
            false
        )
        binding.root.visibility = View.VISIBLE

        binding.rootview.animation = AnimationUtils.loadAnimation(
            context,
            R.anim.item_animation_falldown
        )
        initView(context)
        parentView.addView(binding.root)
    }

}
package com.feylabs.halalkan.customview.imagepreviewcontainer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomviewItemPhotoContainerPreviewBinding
import com.feylabs.halalkan.databinding.CustomviewItemPhotoContainerPreviewSmallBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImage
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL

class CustomViewImageContainerAdapterSmall :
    RecyclerView.Adapter<CustomViewImageContainerAdapterSmall.AdapterViewHolder>() {

    val data = mutableListOf<CustomViewPhotoModel>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<CustomViewPhotoModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun addNewData(data: CustomViewPhotoModel) {
        this.data.add(data)
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class AdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: CustomviewItemPhotoContainerPreviewSmallBinding =
            CustomviewItemPhotoContainerPreviewSmallBinding.bind(itemView)

        fun onBind(model: CustomViewPhotoModel) {
            val mContext = binding.root.context

            binding.root.setOnClickListener {
                model.onclicklistener?.invoke()
            }

            if (model.url.isNotEmpty())
                binding.photo.loadImageFromURL(
                    mContext, url = model.url
                )
            else
                binding.photo.loadImage(mContext, model.drawable)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.customview_item_photo_container_preview_small, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: CustomViewPhotoModel)
    }
}
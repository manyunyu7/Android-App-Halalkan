package com.feylabs.halalkan.view.resto.admin_resto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomviewItemPhotoContainerPreviewSmallBinding as AdapterBinding
import com.feylabs.halalkan.databinding.ItemPhotoBinding
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel as AdapterModel
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL

class PhotoListAdapter : RecyclerView.Adapter<PhotoListAdapter.AdapterViewHolder>() {

    val data = mutableListOf<AdapterModel>()
    lateinit var adapterInterface: ItemInterface
    var page = 1

    fun setWithNewData(data: MutableList<AdapterModel>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        this.data.clear()
        notifyDataSetChanged()
    }

    fun addNewData(newData: MutableList<AdapterModel>, newPage: Int = this.page) {
        newData.forEachIndexed { index, data ->
            this.data.add(data)
            notifyItemInserted(itemCount - 1)
        }
        this.page = newPage
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class AdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: AdapterBinding = AdapterBinding.bind(itemView)

        fun onBind(model: AdapterModel) {
            val mContext = binding.root.context

//            binding.base.animation = AnimationUtils.loadAnimation(
//                mContext,
//                R.anim.fade_transition_animation
//            )

            if (::adapterInterface.isInitialized) {
                binding.root.setOnClickListener {
                    adapterInterface.onclick(model)
                }
            }

            binding.photo.loadImageFromURL(mContext, model.url)
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
        fun onclick(model: AdapterModel)
    }
}
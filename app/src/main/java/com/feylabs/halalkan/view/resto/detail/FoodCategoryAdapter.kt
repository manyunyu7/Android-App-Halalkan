package com.feylabs.halalkan.view.resto.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.data.remote.reqres.resto.FoodCategoryResponse.FoodCategoryResponseItem as AdapterModel
import com.feylabs.halalkan.databinding.CustomViewItemMenuTabBinding as AdapterBinding

class FoodCategoryAdapter : RecyclerView.Adapter<FoodCategoryAdapter.AdapterViewHolder>() {

    val data = mutableListOf<AdapterModel>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<AdapterModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    fun setActiveMenu(menuId: Int) {
        var activeIndex = 0
        data.forEachIndexed { index, foodCategoryResponseItem ->
            foodCategoryResponseItem.isActive = menuId == foodCategoryResponseItem.id
            notifyItemChanged(index)
        }
    }

    inner class AdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: AdapterBinding = AdapterBinding.bind(itemView)

        fun onBInd(model: AdapterModel) {
            val mContext = binding.root.context

            if (model.isActive)
                binding.base.animation = AnimationUtils.loadAnimation(
                    mContext,
                    R.anim.blink
                )

            if (::adapterInterface.isInitialized) {
                binding.root.setOnClickListener {
                    adapterInterface.onclick(model)
                }
            }

            binding.tabMenuName.text = model.name
            if (model.isActive) {
                binding.activeIndicator.visibility = View.VISIBLE
            } else binding.activeIndicator.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_view_item_menu_tab, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.onBInd(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: AdapterModel)
    }
}
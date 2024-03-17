package com.feylabs.halalkan.view.new_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.ItemGridRsBinding
import com.feylabs.halalkan.databinding.ItemHomeRestaurantBinding

class ListRestaurantAdapter :
    RecyclerView.Adapter<ListRestaurantAdapter.RestaurantHomeViewHolder>() {

    val data = mutableListOf<RestaurantHomeUIModel>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<RestaurantHomeUIModel>) {
        this.data.clear()
        this.data.addAll(data.reversed())
    }

    fun setupAdapterInterface(obj:ItemInterface) {
        this.adapterInterface = obj
    }

    inner class RestaurantHomeViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemHomeRestaurantBinding = ItemHomeRestaurantBinding.bind(itemView)

        fun onBInd(model: RestaurantHomeUIModel) {
            val mContext = binding.root.context

            binding.base.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.fade_transition_animation
            )

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.tvDistance.text = model?.distance.toString()

            binding.tvTitle.text = model?.title.toString()
            binding.tvTopCategory.text = model?.categoryTop.toString()
            binding.tvMiddleCategory.text = model?.categoryMiddle.toString()
            binding.tvAddress.text = model?.address

            binding.tvCuisineCategory.text = model.cuisineCategory
            Glide.with(mContext)
                .load(model?.image.toString())
                .thumbnail(Glide.with(mContext).load(R.raw.ic_loading_google).fitCenter())
                .skipMemoryCache(true)
                .into(binding.imgCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_restaurant, parent, false)
        return RestaurantHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantHomeViewHolder, position: Int) {
        holder.onBInd(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: RestaurantHomeUIModel)
    }
}
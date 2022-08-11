package com.feylabs.halalkan.view.new_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidModelResponse
import com.feylabs.halalkan.databinding.ItemHomeMosqueBinding
import com.feylabs.halalkan.utils.ImageViewUtils.imgFullPath
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.StringUtil.decodeMuskoUrl

class ListMasjidAdapter :
    RecyclerView.Adapter<ListMasjidAdapter.MasjidViewHolder>() {

    val data = mutableListOf<MasjidModelResponse>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<MasjidModelResponse>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun sortByDistance(){
        
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class MasjidViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemHomeMosqueBinding = ItemHomeMosqueBinding.bind(itemView)

        fun onBind(model: MasjidModelResponse) {
            val mContext = binding.root.context

            binding.base.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.item_animation_falldown
            )

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.tvTitle.text = model.name
            binding.tvMiddleCategory.text = model.address
            binding.tvRatingCount.text=model.review_avg.toString()

            if (model.distanceKm != null) {
                val distanceRounded = model.distanceKm
                binding.tvDistance.visibility = View.VISIBLE
                binding.tvDistance.text = "$distanceRounded Km"
            } else {
                binding.tvDistance.visibility = View.GONE
            }

            binding.tvTopCategory.text = model.categoryName.uppercase()
            binding.imgCover.loadImageFromURL(mContext,model.img_full_path)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasjidViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_mosque, parent, false)
        return MasjidViewHolder(view)
    }

    override fun onBindViewHolder(holder: MasjidViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: MasjidModelResponse)
    }
}
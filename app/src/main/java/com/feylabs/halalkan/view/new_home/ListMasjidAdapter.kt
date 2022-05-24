package com.feylabs.halalkan.view.new_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.reqres.masjid.DataMasjid
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.databinding.ItemHomeMosqueBinding
import com.feylabs.halalkan.utils.Network
import com.feylabs.halalkan.utils.StringUtil.encodeUrl

class ListMasjidAdapter :
    RecyclerView.Adapter<ListMasjidAdapter.MasjidViewHolder>() {

    val data = mutableListOf<DataMasjid>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<DataMasjid>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj:ItemInterface) {
        this.adapterInterface = obj
    }

    inner class MasjidViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemHomeMosqueBinding = ItemHomeMosqueBinding.bind(itemView)

        fun onBind(model: DataMasjid) {
            val mContext = binding.root.context

            binding.base.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.item_animation_falldown
            )

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.tvTitle.text=model.name
            binding.tvMiddleCategory.text=model.address

            val imgUrl = Network.REAL_URL_V1 + model.img.encodeUrl()
            binding.tvTopCategory.text=model.categoryName.uppercase()

            Glide.with(mContext)
                .load(imgUrl)
                .thumbnail(Glide.with(mContext).load(R.raw.ic_loading_google).fitCenter())
                .skipMemoryCache(true)
                .into(binding.imgCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasjidViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_mosque, parent, false)
        return MasjidViewHolder(view)
    }

    override fun onBindViewHolder(holder: MasjidViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: DataMasjid)
    }
}
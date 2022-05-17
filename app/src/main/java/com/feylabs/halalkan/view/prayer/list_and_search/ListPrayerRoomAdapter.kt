package com.feylabs.halalkan.view.prayer.list_and_search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.ItemPrayerRoomListSearchBinding
import com.feylabs.halalkan.utils.Network.REAL_URL
import com.feylabs.halalkan.view.prayer.PrayerRoomListUIModel

class ListPrayerRoomAdapter :
    RecyclerView.Adapter<ListPrayerRoomAdapter.ListPrayerRoomViewHolder>() {

    val data = mutableListOf<PrayerRoomListUIModel>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<PrayerRoomListUIModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class ListPrayerRoomViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemPrayerRoomListSearchBinding =
            ItemPrayerRoomListSearchBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun onBInd(model: PrayerRoomListUIModel) {
            val mContext = binding.root.context

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.tvTitle.text = model?.title.toString()
            binding.tvAddress.text = model?.address + model.categoryMiddle

            Glide.with(mContext)
                .load(REAL_URL+"uploads/img/masjids/" + model?.image.toString())
                .thumbnail(Glide.with(mContext).load(R.raw.ic_loading_google).fitCenter())
                .skipMemoryCache(true)
                .into(binding.ivMainImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPrayerRoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prayer_room_list_search, parent, false)
        return ListPrayerRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListPrayerRoomViewHolder, position: Int) {
        holder.onBInd(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: PrayerRoomListUIModel)
    }
}
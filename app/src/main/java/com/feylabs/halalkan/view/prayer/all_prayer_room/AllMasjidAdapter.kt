package com.feylabs.halalkan.view.prayer.all_prayer_room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.ItemPrayerRoomBinding as AdapterBinding
import  com.feylabs.halalkan.data.remote.reqres.masjid.DataMasjid as AdapterData
import com.feylabs.halalkan.databinding.ItemRvLoadMoreBinding
import com.feylabs.halalkan.utils.Network
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.feylabs.halalkan.utils.PaginationPlaceholder.Companion.VFooter
import com.feylabs.halalkan.utils.PaginationPlaceholder.Companion.VNormal
import com.feylabs.halalkan.utils.StringUtil.encodeUrl

class AllMasjidAdapter :
    RecyclerView.Adapter<AllMasjidAdapter.ManyunyuViewHolder>() {

    val data = mutableListOf<AdapterData>()
    var page = 1
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<AdapterData>) {
        this.data.clear()
        this.data.addAll(data)
        this.data.add(getLastPlaceholder())
        this.notifyDataSetChanged()
    }

    fun clearData() {
        this.data.clear()
        notifyDataSetChanged()
    }

    fun addNewData(newData: MutableList<AdapterData>, newPage: Int = this.page) {
        this.data.forEachIndexed { index, mData ->
            if (mData.ViewType == VFooter) {
                this.data[index].isFooterVisible = false
            }
            notifyItemChanged(index)
        }
        newData.forEachIndexed { index, data ->
            this.data.add(data)
            notifyItemInserted(itemCount-1)
        }
        this.data.add(getLastPlaceholder())
        notifyItemInserted(itemCount-1)
        this.page = newPage
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class ManyunyuViewHolder(v: View, val viewType: Int) : RecyclerView.ViewHolder(v) {
        fun onBind(model: AdapterData) {
            when (viewType) {
                VFooter -> {
                    renderFooter(model)
                }
                VNormal -> {
                    renderNormal(model)
                }
                else -> {
                    renderNormal(model)
                }
            }
        }

        private fun loadMore() {
            adapterInterface.loadMore(page)
        }

        private fun renderFooter(model: AdapterData) {
            val binding: ItemRvLoadMoreBinding = ItemRvLoadMoreBinding.bind(itemView)
            if (!model.isFooterVisible) {
                itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            }
            binding.btnLoadMore.setOnClickListener {
                adapterInterface.loadMore(page)
                val context = binding.root.context
                binding.btnLoadMore.text = "Halaman $page"
            }
        }

        private fun renderNormal(model: AdapterData) {
            val binding: AdapterBinding = AdapterBinding.bind(itemView)
            val mContext = binding.root.context

            binding.tvRatingCount.text = model.review_avg

            binding.root.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.fade_transition_animation
            )

            binding.tvTitle.text = model.name
            binding.tvAddress.text = model.address
            val imgUrl = Network.REAL_URL_V1 + model.img.encodeUrl()
            binding.tvCategory.text = model.categoryName.uppercase()

            if (model.distanceKm != null) {
                val distanceRounded = model.distanceKm.toString().replaceAfter(".", "")
                binding.dotSeparator.visibility = View.VISIBLE

                binding.tvDistance.visibility = View.VISIBLE
                binding.tvDistance.text = "$distanceRounded Km"
            } else {
                binding.dotSeparator.visibility = View.GONE
                binding.tvDistance.visibility = View.GONE
            }

            Glide.with(mContext)
                .load(imgUrl)
                .thumbnail(Glide.with(mContext).load(R.raw.ic_loading_google).fitCenter())
                .skipMemoryCache(true)
                .into(binding.ivMainImage)

            binding.root.setOnClickListener {
                if (::adapterInterface.isInitialized)
                    adapterInterface.onclick(model)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].ViewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManyunyuViewHolder {
        val rowView: View = when (viewType) {
            VNormal -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_prayer_room, parent, false)
            VFooter -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rv_load_more, parent, false)
            else -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_prayer_room, parent, false)
        }

        return ManyunyuViewHolder(rowView, viewType)
    }

    override fun onBindViewHolder(holder: ManyunyuViewHolder, position: Int) {
        holder.setIsRecyclable(false);
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: AdapterData)
        fun loadMore(page: Int)
    }

    private fun getLastPlaceholder(): AdapterData {
        return PaginationPlaceholder.getMasjidDataPaginationResponsePlaceHolder()
    }
}
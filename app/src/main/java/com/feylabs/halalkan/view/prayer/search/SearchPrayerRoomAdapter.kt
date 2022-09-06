package com.feylabs.halalkan.view.prayer.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import com.feylabs.halalkan.databinding.ItemPrayerRoomCompactBinding as Binding
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidModelResponse as Data
import com.feylabs.halalkan.databinding.ItemPrayerRoomBinding
import com.feylabs.halalkan.utils.CommonUtil
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.like.LikeButton
import com.like.OnLikeListener


class SearchPrayerRoomAdapter :
    RecyclerView.Adapter<SearchPrayerRoomAdapter.ManyunyuViewHolder>() {

    val data = mutableListOf<Data>()
    var page = 1
    lateinit var adapterInterface: ItemInterface
    var isFromLike: Boolean = false

    // Allows to remember the last item shown on screen
    private val lastPosition = -1

    fun setWithNewData(data: MutableList<Data>) {
        this.data.clear()
        addNewData(data)
    }

    fun clearData() {
        this.data.clear()
        notifyDataSetChanged()
    }


    fun addNewData(newData: MutableList<Data>, newPage: Int = this.page) {
        newData.forEachIndexed { index, data ->
            if (isFromLike) {
                if (data.is_favorited) {
                    this.data.add(data)
                    notifyItemInserted(itemCount - 1)
                }
            } else {
                this.data.add(data)
                notifyItemInserted(itemCount - 1)
            }
        }
        this.page = newPage
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class ManyunyuViewHolder(v: View, val viewType: Int) : RecyclerView.ViewHolder(v) {
        fun onBind(model: Data) {
            renderNormal(model)
        }

        private fun renderNormal(model: Data) {
            val binding: Binding = Binding.bind(itemView)
            val mContext = binding.root.context

            binding.tvRatingCount.text = model.review_avg

            binding.root.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.fade_transition_animation
            )

            if (CommonUtil.isLoggedIn(mContext)) {
                binding.btnLike.isLiked = model.is_favorited
                binding.btnLike.isEnabled = true

                binding.btnLike.setOnLikeListener(object : OnLikeListener {
                    override fun liked(likeButton: LikeButton) {
                        if (::adapterInterface.isInitialized) {
                            adapterInterface.onLike(model)
                        }
                    }

                    override fun unLiked(likeButton: LikeButton) {
                        if (::adapterInterface.isInitialized) {
                            adapterInterface.onUnLike(model)
                            if (isFromLike) {
                                data.removeAt(adapterPosition)
                                notifyItemRemoved(adapterPosition)
                            }
                        }

                    }
                })
            } else binding.btnLike.isEnabled = false

            binding.tvTitle.text = model.name
            binding.tvAddress.text = model.address

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

            binding.ivMainImage.loadImageFromURL(mContext,model.img_full_path)
            binding.tvRatingCount.text=model.review_avg.toString()

            binding.root.setOnClickListener {
                if (::adapterInterface.isInitialized)
                    adapterInterface.onclick(model)
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return data[position]?.ViewType ?: return PaginationPlaceholder.VFooter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManyunyuViewHolder {
        val rowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prayer_room_compact, parent, false)
        return ManyunyuViewHolder(rowView, viewType)
    }

    override fun onBindViewHolder(holder: ManyunyuViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onLike(model: Data)
        fun onUnLike(model: Data)
        fun onclick(model: Data)
        fun loadMore(page: Int)
    }

}
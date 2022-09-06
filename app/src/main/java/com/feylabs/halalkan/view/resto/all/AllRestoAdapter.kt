package com.feylabs.halalkan.view.resto.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.databinding.ItemRestoCompactBinding  as Binding
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse  as Data
import com.feylabs.halalkan.utils.CommonUtil
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.like.LikeButton
import com.like.OnLikeListener


class AllRestoAdapter :
    RecyclerView.Adapter<AllRestoAdapter.ManyunyuViewHolder>() {

    val data = mutableListOf<Data>()
    var page = 1
    lateinit var adapterInterface: ItemInterface

    // Allows to remember the last item shown on screen
    private val lastPosition = -1

    fun setWithNewData(data: MutableList<Data>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        this.data.clear()
        notifyDataSetChanged()
    }

    fun addNewData(newData: MutableList<Data>, newPage: Int = this.page) {
        newData.forEachIndexed { index, data ->
            this.data.add(data)
            notifyItemInserted(itemCount - 1)
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


        private fun renderNormal(model: Data?) {
            val binding: Binding = Binding.bind(itemView)
            val mContext = binding.root.context

            model?.let {
                binding.root.animation = AnimationUtils.loadAnimation(
                    mContext,
                    R.anim.fade_transition_animation
                )

                if (CommonUtil.isLoggedIn(mContext)) {
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
                            }

                        }
                    })
                } else binding.btnLike.isEnabled = false

                binding.tvTitle.text = model.name.toString()
                binding.tvCategory.text = model.certificationName.toString()
                binding.ivMainImage.loadImageFromURL(mContext, model.image_full_path)

            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position]?.ViewType ?: return PaginationPlaceholder.VFooter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManyunyuViewHolder {
        val rowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resto_compact, parent, false)
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
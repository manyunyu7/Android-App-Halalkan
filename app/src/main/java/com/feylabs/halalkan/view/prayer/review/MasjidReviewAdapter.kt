package com.feylabs.halalkan.view.prayer.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse.Reviews.Data
import com.feylabs.halalkan.databinding.ItemReviewBinding
import com.feylabs.halalkan.databinding.ItemRvLoadMoreBinding
import com.feylabs.halalkan.utils.ImageViewUtils.imgFullUserPath
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.feylabs.halalkan.utils.PaginationPlaceholder.Companion.VFooter
import com.feylabs.halalkan.utils.PaginationPlaceholder.Companion.VNormal


class MasjidReviewAdapter :
    RecyclerView.Adapter<MasjidReviewAdapter.ManyunyuViewHolder>() {

    val data = mutableListOf<Data>()
    var page = 1
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<Data>) {
        this.data.clear()
        this.data.addAll(data)
        this.data.add(getLastPlaceholder())
    }

    fun addNewData(newData: MutableList<Data>, newPage: Int = this.page) {
        this.data.addAll(newData)
        this.data.forEachIndexed { index, mData ->
            if (mData.ViewType == VFooter) {
                this.data[index].isFooterVisible = false
            }
        }
        this.data.add(getLastPlaceholder())
        this.page = newPage
        notifyDataSetChanged()
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class ManyunyuViewHolder(v: View, val viewType: Int) : RecyclerView.ViewHolder(v) {
        fun onBind(model: Data) {
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

        private fun renderFooter(model: Data) {
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

        private fun renderNormal(model: Data) {
            val binding: ItemReviewBinding = ItemReviewBinding.bind(itemView)
            val mContext = binding.root.context

            /**
            binding.root.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.fade_transition_animation
            )
             */

            binding.tvQuestion.text = model.comment


            val listPhotos = mutableListOf<CustomViewPhotoModel>()
            model.reviewPhotos.forEachIndexed { index, any ->
                if (any is String){
                    listPhotos.add(CustomViewPhotoModel(
                        url = any
                    ))
                }
            }

            binding.imagePreview.replaceAllImage(listPhotos)

            model.userInfo?.let {user->
                binding.includeUserProfile.apply {
                   labelUserName.text = user.name
                    labelCategory.text = model.ratingId.toString()
                    labelTime.text = model.createdAt
                    ivMainImage.loadImageFromURL(
                        mContext, user.photo.imgFullUserPath()
                    )
                }
            }


            binding.root.setOnClickListener {
                if (adapterInterface != null)
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
                .inflate(R.layout.item_review, parent, false)
            VFooter -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rv_load_more, parent, false)
            else -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_review, parent, false)
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
        fun onclick(model: Data)
        fun loadMore(page: Int)
    }

    private fun getLastPlaceholder(): Data {
        return PaginationPlaceholder.getMasjidReviewPaginationResponsePlaceHolder()
    }
}
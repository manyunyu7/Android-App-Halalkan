package com.feylabs.halalkan.view.forum.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.reqres.forum.ForumModelResponse as Data
import com.feylabs.halalkan.databinding.ItemRvLoadMoreBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.PaginationPlaceholder.Companion.VFooter
import com.feylabs.halalkan.utils.PaginationPlaceholder.Companion.VNormal
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.feylabs.halalkan.utils.PaginationPlaceholder.Companion.getForumPlaceholder
import com.feylabs.halalkan.databinding.ItemForumBinding as Binding


class ForumAdapter :
    RecyclerView.Adapter<ForumAdapter.ManyunyuViewHolder>() {

    val data = mutableListOf<Data>()
    var page = 1
    lateinit var adapterInterface: ItemInterface

    // Allows to remember the last item shown on screen
    private val lastPosition = -1

    fun setWithNewData(data: MutableList<Data>) {
        this.data.clear()
        this.data.addAll(data)
        this.data.add(getLastPlaceholder())
        notifyDataSetChanged()
    }

    fun clearData() {
        this.data.clear()
        notifyDataSetChanged()
    }

    fun addNewData(newData: MutableList<Data>, newPage: Int = this.page) {
        this.data.forEachIndexed { index, mData ->
            mData?.let {
                if (mData.ViewType == VFooter) {
                    this.data[index]?.isFooterVisible = false
                }
                notifyItemChanged(index)
            }
        }
        newData.forEachIndexed { index, data ->
            this.data.add(data)
            notifyItemInserted(itemCount - 1)
        }
        this.data.add(getLastPlaceholder())
        notifyItemInserted(itemCount - 1)
        this.page = newPage
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class ManyunyuViewHolder(v: View, val viewType: Int) : RecyclerView.ViewHolder(v) {
        fun onBind(model: Data?) {
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

        private fun renderFooter(model: Data?) {
            val binding: ItemRvLoadMoreBinding = ItemRvLoadMoreBinding.bind(itemView)

            model?.let {
                if (!model.isFooterVisible) {
                    itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
                }
            } ?: run {
                itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            }

            binding.btnLoadMore.setOnClickListener {
                adapterInterface.loadMore(page)
                val context = binding.root.context
                binding.btnLoadMore.text = "Halaman $page"
            }
        }

        private fun renderNormal(model: Data?) {
            val binding: Binding = Binding.bind(itemView)
            val mContext = binding.root.context

            model?.let {
                binding.root.animation = AnimationUtils.loadAnimation(
                    mContext,
                    R.anim.fade_transition_animation
                )

                binding.tvQuestion.text = model.body.toString()
                binding.includeUserProfile.labelCategory.text = model.category?.name
                binding.tvTitle.text = model.title.toString()

                if (model.img == null) {
                    binding.containerCover.visibility = View.GONE
                } else
                    binding.photo.loadImageFromURL(mContext, model.img_full_path)


                model.likes?.size?.let {
                    binding.tvLikeCount.text = it.toString()
                }

                model.comments?.size?.let {
                    binding.tvCommentCount.text = it.toString()
                }


                model.user?.let { user ->
                    binding.includeUserProfile.apply {
                        labelUserName.text = user.name
                        labelTime.text = model.createdAt
                        ivMainImage.loadImageFromURL(
                            mContext, user.imgFullPath
                        )
                    }
                }


                binding.root.setOnClickListener {
                    if (adapterInterface != null)
                        adapterInterface.onclick(model)
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position]?.ViewType ?: return PaginationPlaceholder.VFooter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManyunyuViewHolder {
        val rowView: View = when (viewType) {
            VNormal -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_forum, parent, false)
            VFooter -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rv_load_more, parent, false)
            else -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_forum, parent, false)
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
        return getForumPlaceholder()
    }
}
package com.feylabs.halalkan.view.forum.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.reqres.resto.RestoReviewPaginationResponse
import com.feylabs.halalkan.databinding.ItemRvLoadMoreBinding
import com.feylabs.halalkan.utils.CommonUtil
import com.feylabs.halalkan.utils.CommonUtil.showSnackbar
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.PaginationPlaceholder
import com.feylabs.halalkan.utils.PaginationPlaceholder.Companion.VFooter
import com.feylabs.halalkan.utils.PaginationPlaceholder.Companion.VNormal
import com.feylabs.halalkan.utils.PaginationPlaceholder.Companion.getForumPlaceholder
import com.like.LikeButton
import com.like.OnLikeListener
import com.feylabs.halalkan.data.remote.reqres.forum.ForumModelResponse as Data
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
                                binding.tvLikeCount.text = (model.getLikeCount() + 1).toString()
                            }
                        }

                        override fun unLiked(likeButton: LikeButton) {
                            if (::adapterInterface.isInitialized) {
                                adapterInterface.onUnLike(model)
                                val currentLikeCount =
                                    binding.tvLikeCount.text.toString().toIntOrNull() ?: 0
                                if (currentLikeCount != 0) {
                                    binding.tvLikeCount.text =
                                        (currentLikeCount - 1).toString()
                                }
                            }

                        }
                    })
                } else binding.btnLike.isEnabled = false


                if (model.getLikeCount() == 0) {
                    binding.btnLike.isLiked = false
                }

                binding.btnActionForum.setOnClickListener {
                    if (::adapterInterface.isInitialized)
                        adapterInterface.onAction(model)
                }

                binding.cardForum.setOnClickListener{
                    if (::adapterInterface.isInitialized)
                        adapterInterface.onclick(model)
                }


                binding.tvQuestion.text = model.body.toString()
                binding.includeUserProfile.labelCategory.text = model.category?.name
                binding.tvTitle.text = model.title.toString()

                if (model.img == null) {
                    binding.containerCover.visibility = View.GONE
                } else {
                    binding.containerCover.visibility = View.VISIBLE
                    binding.photo.loadImageFromURL(mContext, model.img_full_path)
                }


                model.likes?.let {
                    var isUserLiked = false
                    it.size.let {
                        binding.tvLikeCount.text = it.toString()
                    }

                    it.forEachIndexed { index, like ->
                        if (like.userId.toString() == MyPreference(mContext).getUserID()) {
                            isUserLiked = true
                        }
                    }

                    if (isUserLiked) {
                        binding.btnLike.isLiked = true
                    }
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


            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position]?.ViewType ?: return PaginationPlaceholder.VFooter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManyunyuViewHolder {
        val rowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forum, parent, false)
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
        fun onShare(model: Data)
        fun onclick(model: Data)
        fun onAction(model: Data)
        fun loadMore(page: Int)
    }

}
package com.feylabs.halalkan.view.forum.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.utils.CommonUtil
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.like.LikeButton
import com.like.OnLikeListener
import com.feylabs.halalkan.data.remote.reqres.forum.ForumCommentResponse.ForumCommentResponseItem as AdapterModel
import com.feylabs.halalkan.databinding.ItemForumCommentBinding as AdapterBinding

class ForumCommentAdapter : RecyclerView.Adapter<ForumCommentAdapter.AdapterViewHolder>() {

    val data = mutableListOf<AdapterModel>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<AdapterModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class AdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: AdapterBinding = AdapterBinding.bind(itemView)

        fun onBInd(model: AdapterModel) {
            val mContext = binding.root.context

            binding.root.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.blink
            )

            if (::adapterInterface.isInitialized) {
                binding.root.setOnClickListener {
                    adapterInterface.onclick(model,adapterPosition)
                }
            }

            if (CommonUtil.isLoggedIn(mContext)) {
                binding.btnCommentLike.isEnabled = true
                binding.btnCommentLike.setOnLikeListener(object : OnLikeListener {
                    override fun liked(likeButton: LikeButton) {
                        if (::adapterInterface.isInitialized) {
                            val currentLikeCount = binding.tvCommentLikeCount.text.toString().toIntOrNull()?:0
                            adapterInterface.onLiked(model)
                            binding.tvCommentLikeCount.text = (currentLikeCount + 1).toString()
                        }
                    }

                    override fun unLiked(likeButton: LikeButton) {
                        if (::adapterInterface.isInitialized) {
                            adapterInterface.onUnliked(model)
                            val currentLikeCount =
                                binding.tvCommentLikeCount.text.toString().toIntOrNull() ?: 0
                            if (currentLikeCount != 0) {
                                binding.tvCommentLikeCount.text =
                                    (currentLikeCount - 1).toString()
                            }
                        }

                    }
                })
            } else binding.btnCommentLike.isEnabled = false

            model.likes?.let {
                var isUserLiked = false
                it.size.let {
                    binding.tvCommentLikeCount.text = it.toString()
                }

                it.forEachIndexed { index, like ->
                    if (like.userId.toString() == MyPreference(mContext).getUserID()) {
                        isUserLiked = true
                    }
                }

                binding.btnCommentLike.isLiked = isUserLiked
            }

            binding.tvComment.text = model.comment.toString()
            model.likes?.let {
                binding.tvCommentLikeCount.text = it.size.toString()
            }

            model.user?.let {
                binding.ivMainImage.loadImageFromURL(mContext, it.imgFullPath)
                binding.labelUserName.text = it.name
            }

            binding.labelCommentTime.text = model.createdAt

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forum_comment, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.onBInd(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: AdapterModel,position: Int)
        fun onLiked(model: AdapterModel)
        fun onUnliked(model: AdapterModel)
    }
}
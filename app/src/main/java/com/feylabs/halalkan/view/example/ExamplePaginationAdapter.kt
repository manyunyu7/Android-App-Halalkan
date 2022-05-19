package com.kemendag.ppid.ui.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.reqres.MenteeResponse
import com.feylabs.halalkan.databinding.ItemForumBinding
import com.feylabs.halalkan.databinding.ItemRvLoadMoreBinding


class ExamplePaginationAdapter :
    RecyclerView.Adapter<ExamplePaginationAdapter.ManyunyuViewHolder>() {

    companion object VIEW_TYPES {
        const val Normal = 2
        const val Footer = 3
    }

    val data = mutableListOf<MenteeResponse.Data>()
    var page = 1
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<MenteeResponse.Data>) {
        this.data.clear()
        this.data.addAll(data)
        this.data.add(getLastMenteePlaceholder())
        notifyDataSetChanged()
    }

    fun addNewData(data: MutableList<MenteeResponse.Data>, newPage: Int = this.page) {
        this.data.addAll(data)
        this.data.forEachIndexed { index, mData ->
            if (mData.ViewType == Footer) {
                this.data[index].isVisible = false
            }
        }
        this.data.add(getLastMenteePlaceholder())
        this.page = newPage
        notifyDataSetChanged()
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class ManyunyuViewHolder(v: View, val viewType: Int) : RecyclerView.ViewHolder(v) {
        fun onBind(model: MenteeResponse.Data) {
            when (viewType) {
                Footer -> {
                    renderFooter(model)
                }
                Normal -> {
                    renderNormal(model)
                }
                else -> {
                    renderNormal(model)
                }
            }
        }

        private fun renderFooter(model: MenteeResponse.Data) {
            val binding: ItemRvLoadMoreBinding = ItemRvLoadMoreBinding.bind(itemView)

            if (!model.isVisible) {
                binding.root.visibility=View.GONE
            }
            binding.btnLoadMore.setOnClickListener {
                adapterInterface.loadMore(page)
                val context = binding.root.context
//                binding.btnLoadMore.text = context.getString(R.string.Halaman) + " $page"
                binding.btnLoadMore.visibility = View.GONE
            }
        }

        private fun renderNormal(model: MenteeResponse.Data) {
            val binding: ItemForumBinding = ItemForumBinding.bind(itemView)
            val mContext = binding.root.context
//            binding.base.animation = AnimationUtils.loadAnimation(
//                mContext,
//                R.anim.fade_transition_animation
//            )

            binding.tvTitle.text = model.nama.toString()

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
            Normal -> LayoutInflater.from(parent.context)
                .inflate(R.layout.item_forum, parent, false)
            Footer -> LayoutInflater.from(parent.context)
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
        fun onclick(model: MenteeResponse.Data)
        fun loadMore(page: Int)
    }

    private fun getLastMenteePlaceholder(): MenteeResponse.Data {
        return MenteeResponse.Data(
            fakultas = "",
            nama = "",
            id = 1,
            createdAt = "",
            updatedAt = "",
            jk = 0,
            kelas = "",
            kelompokId = 1,
            lineId = "",
            nim = "",
            noTelp = "",
            programStudi = "",
            ViewType = ExamplePaginationAdapter.Footer
        )
    }
}
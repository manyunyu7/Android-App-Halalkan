package com.feylabs.halalkan.view.utilview.searchwithimage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomViewItemSearchLanguageDialogBinding

class ListSearchWithImageAdapter :
    RecyclerView.Adapter<ListSearchWithImageAdapter.AdapterViewHolder>() {

    val data = mutableListOf<SearchWithImageModel>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<SearchWithImageModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class AdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: CustomViewItemSearchLanguageDialogBinding =
            CustomViewItemSearchLanguageDialogBinding.bind(itemView)

        fun onBInd(model: SearchWithImageModel) {
            val mContext = binding.root.context

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.text.text = model.name
            if (model.imageUrl != "") {
                Glide.with(mContext)
                    .load(model.imageUrl.toString())
                    .thumbnail(Glide.with(mContext).load(R.raw.ic_loading_google).fitCenter())
                    .skipMemoryCache(true)
                    .into(binding.image)

            }else{
                binding.image.visibility=View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_view_item_search_language_dialog, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.onBInd(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: SearchWithImageModel)
    }
}
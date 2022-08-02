package com.feylabs.halalkan.customview.menu_tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomViewItemMenuTabBinding
import com.feylabs.halalkan.databinding.CustomViewItemSearchLanguageDialogBinding
import com.feylabs.halalkan.utils.TranslatorUtil

class MenuTabAdapter :
    RecyclerView.Adapter<MenuTabAdapter.AdapterViewHolder>() {

    val data = mutableListOf<MenuTabModel>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<MenuTabModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun addData(data:MenuTabModel){
        this.data.add(data)
        if(this.data.size==1){
            notifyDataSetChanged()
        } else {
            notifyItemInserted(itemCount-1)
        }
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class AdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: CustomViewItemMenuTabBinding =
            CustomViewItemMenuTabBinding.bind(itemView)

        fun onBind(model: MenuTabModel) {
            val mContext = binding.root.context

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.tabMenuName.text = model.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_view_item_menu_tab, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: MenuTabModel)
    }
}
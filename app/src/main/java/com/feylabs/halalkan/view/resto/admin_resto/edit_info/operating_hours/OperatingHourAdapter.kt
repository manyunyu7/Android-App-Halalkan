package com.feylabs.halalkan.view.resto.admin_resto.edit_info.operating_hours

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.reqres.resto.operating_hour.RestoOperatingHourResponse.Data as Data
import com.feylabs.halalkan.databinding.ItemOperatingHoursBinding  as Binding
import com.feylabs.halalkan.utils.PaginationPlaceholder


class OperatingHourAdapter :
    RecyclerView.Adapter<OperatingHourAdapter.ManyunyuViewHolder>() {

    val data = mutableListOf<Data>()
    var page = 1
    lateinit var adapterInterface: ItemInterface


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

        private fun renderNormal(model: Data) {
            val binding: Binding = Binding.bind(itemView)
            val mContext = binding.root.context

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.tvDay.text=model.translateDay(mContext)
            binding.tvOperatingHour.text="${model.hourStart} : ${model.hourEnd}"

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManyunyuViewHolder {
        val rowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_operating_hours, parent, false)
        return ManyunyuViewHolder(rowView, viewType)
    }

    override fun onBindViewHolder(holder: ManyunyuViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: Data)
    }

}
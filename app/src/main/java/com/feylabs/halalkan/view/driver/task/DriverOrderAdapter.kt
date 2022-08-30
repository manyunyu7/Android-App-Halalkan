package com.feylabs.halalkan.view.driver.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryModel  as AdapterModel
import com.feylabs.halalkan.databinding.ItemHistoryOrderBinding as AdapterBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.resto.RestoUtility.getStatusColor
import com.feylabs.halalkan.view.resto.order.history.HistoryOrderAdapter

class DriverOrderAdapter : RecyclerView.Adapter<DriverOrderAdapter.AdapterViewHolder>() {

    val data = mutableListOf<AdapterModel>()
    lateinit var adapterInterface: ItemInterface

    var page = 1

    fun setWithNewData(data: MutableList<AdapterModel>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun addNewData(newData: MutableList<AdapterModel>, newPage: Int = this.page) {
        newData.forEachIndexed { index, data ->
            this.data.add(data)
            notifyItemInserted(itemCount - 1)
        }
        this.page = newPage
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class AdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: AdapterBinding = AdapterBinding.bind(itemView)

        fun onBInd(model: AdapterModel) {
            val mContext = binding.root.context

            binding.base.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.fade_transition_animation
            )

            if (::adapterInterface.isInitialized) {
                binding.root.setOnClickListener {
                    adapterInterface.onclick(model)
                }
            }

            binding.tvMainName.text = model.restoObj.name

            binding.photo.loadImageFromURL(mContext, model.restoObj.image_full_path)
            model.userObj?.let {
                binding.tvMainName.text= it.name
                binding.tvSecondaryName.text= it.phoneNumber
                binding.photo.loadImageFromURL(mContext,it.imgFullPath)
            }

            binding.orderDate.text=model.createdAt.toString()

            binding.orderStatus.setTextColor(model.statusId.getStatusColor());
            binding.orderedItems.text = model.getOrdersString()
            binding.orderStatus.text = model.statusDesc
            binding.totalPrice.text = model.getFormattedTotalPrice()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_order, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.onBInd(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: AdapterModel)
    }
}
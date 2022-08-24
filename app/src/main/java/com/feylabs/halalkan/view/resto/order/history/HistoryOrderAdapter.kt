package com.feylabs.halalkan.view.resto.order.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.CommonUtil.makeVisible
import com.feylabs.halalkan.data.remote.reqres.order.history.OrderHistoryModel  as AdapterModel
import com.feylabs.halalkan.databinding.ItemHistoryOrderBinding as AdapterBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.resto.RestoUtility
import com.feylabs.halalkan.utils.resto.RestoUtility.getStatusColor

class HistoryOrderAdapter : RecyclerView.Adapter<HistoryOrderAdapter.RestaurantHomeViewHolder>() {

    val data = mutableListOf<AdapterModel>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<AdapterModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class RestaurantHomeViewHolder(v: View) : RecyclerView.ViewHolder(v) {

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

            binding.containerResto.makeVisible()

            binding.orderDate.text=model.createdAt.toString()

            binding.orderStatus.setTextColor(model.statusId.getStatusColor());
            binding.orderedItems.text = model.getOrdersString()
            binding.orderStatus.text = model.statusDesc
            binding.totalPrice.text = model.getFormattedTotalPrice()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history_order, parent, false)
        return RestaurantHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantHomeViewHolder, position: Int) {
        holder.onBInd(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: AdapterModel)
    }
}
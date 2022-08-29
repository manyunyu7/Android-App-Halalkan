package com.feylabs.halalkan.view.resto.admin_resto.driver

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.databinding.ItemDriverBinding  as AdapterBinding
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel as AdapterModel

class ManageDriverAdapter : RecyclerView.Adapter<ManageDriverAdapter.AdapterViewHolder>() {

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

            binding.base.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.fade_transition_animation
            )


            if (::adapterInterface.isInitialized) {
                binding.root.setOnClickListener {
                    adapterInterface.onclick(model)
                }
            }
            binding.tvDriverName.text = model.name
            binding.tvDriverEmail.text=model.email
            binding.tvDriverContact.text=model.phoneNumber

            if (model.isDriverAvailable==0){
                binding.tvAvailability.setTextColor(ContextCompat.getColor(mContext,R.color.halalkan_primary))
                binding.tvAvailability.text=mContext.getString(R.string.title_available)
            }else{
                binding.tvAvailability.setTextColor(ContextCompat.getColor(mContext,R.color.uikit_red_light))
                binding.tvAvailability.text=mContext.getString(R.string.title_unavailable)
            }

            binding.photo.loadImageFromURL(mContext, model.imgFullPath)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_driver, parent, false)
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
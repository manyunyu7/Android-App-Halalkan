package com.feylabs.halalkan.view.resto.admin_resto.init

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.ItemCardRestoAdminBinding  as AdapterBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse as AdapterModel


class RestoAdminMainAdapter :
    RecyclerView.Adapter<RestoAdminMainAdapter.RestaurantHomeViewHolder>() {

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

            if (::adapterInterface.isInitialized)
                binding.root.setOnClickListener {
                    adapterInterface.onclick(model)
                }

            binding.tvDistance.text = model.distanceKm.toString()

            binding.tvTitle.text = model.name.toString()
            binding.tvTopCategory.text = "Apayo"
            binding.tvAddress.text = model.address

            if (model.distanceKm.isNullOrEmpty()){
                binding.containerDistance.visibility=View.GONE
            }

            binding.containerRating.visibility=View.GONE

            binding.imgCover.loadImageFromURL(mContext,model.image_full_path)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_resto_admin, parent, false)
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
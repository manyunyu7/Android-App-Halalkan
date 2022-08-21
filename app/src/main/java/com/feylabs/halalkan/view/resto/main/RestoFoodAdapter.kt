package com.feylabs.halalkan.view.resto.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.CustomPlusMinus
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.CommonUtil.makeVisible
import com.feylabs.halalkan.databinding.ItemFoodBinding as AdapterBinding
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse as AdapterModel
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.resto.OrderLocalModel
import com.feylabs.halalkan.utils.resto.OrderUtility
import com.feylabs.halalkan.utils.snackbar.UtilSnackbar.showSnackbar
import com.tangxiaolv.telegramgallery.Utils.AndroidUtilities.showToast


class RestoFoodAdapter : RecyclerView.Adapter<RestoFoodAdapter.RestaurantHomeViewHolder>() {

    val data = mutableListOf<AdapterModel>()
    lateinit var adapterInterface: ItemInterface
    lateinit var orderInterface: OrderInterface

    fun setWithNewData(data: MutableList<AdapterModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    fun setupAdapterInterface(obj: OrderInterface) {
        this.orderInterface = obj
    }

    inner class RestaurantHomeViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: AdapterBinding = AdapterBinding.bind(itemView)
        var mContext = binding.root.context

        fun onBInd(model: AdapterModel) {
            val mContext = binding.root.context

            val orderUtility = OrderUtility(mContext)


            if (MyPreference(mContext).getUserRole().toString() != "2") {
                binding.btnOrder.visibility = View.GONE
            }

            binding.btnNotes.setOnClickListener {
                showSnackbar(binding.root, orderUtility.getListOrder().toString())
            }


            // check if menu is already inserted
            if (orderUtility.isItemAlreadyInserted(model.id.toString())) {
                model.isOrdered = true
                val item = orderUtility.getItem(model.id.toString())
                item?.let {
                    model.notes = it.notes
                    model.orderedQuantity = it.quantity
                }
            }else{
                model.isOrdered=false
            }

            //handle view for ordered items to distinguish order status
            if (model.isOrdered.not()){
                setToOrdered(false)
            }else{
                setToOrdered(true)
            }

            binding.btnOrder.setOnClickListener {
                binding.customPlusMinus.addOne()
                setToOrdered(true)
                orderInterface.onchange()
            }


            binding.customPlusMinus.customPlusMinusInterface =
                object : CustomPlusMinus.OnQuantityChangeListener {
                    override fun onChange(quantity: Int) {

                        model.orderedQuantity = quantity
                        binding.customPlusMinus.setQuantity(quantity)

                        if (quantity > 0) {
                            orderUtility.addItem(
                                OrderLocalModel(
                                    menuId = model.id.toString(),
                                    quantity = quantity,
                                    notes = model.notes.toString(),
                                    price = model.price.toDouble()
                                )
                            )
                        }

                        if (quantity == 0) {
                            setToOrdered(false)
                            orderUtility.removeItem(model.id.toString())
                        }
                        orderInterface.onchange()
                    }
                }

            binding.customPlusMinus.setQuantity(
                model.orderedQuantity
            )

            binding.root.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.fade_transition_animation
            )

            if (::adapterInterface.isInitialized)
                binding.root.setOnClickListener {
                    adapterInterface.onclick(model)
                }

            binding.tvTitle.text = model.name
            binding.tvPrice.text = model.price.toString()
            binding.tvFoodDesc.text = model.description
            binding.ivMainImage.loadImageFromURL(mContext, model.imgFullPath)


        }

        private fun setToOrdered(b: Boolean) {
            if (b){
                binding.btnNotes.makeGone()
                binding.customPlusMinus.makeVisible()
                binding.btnNotes.makeVisible()
                binding.btnOrder.makeGone()

//                binding.btnNotes.animation = AnimationUtils.loadAnimation(
//                    mContext,
//                    R.anim.fade_transition_animation
//                )
//
//                binding.customPlusMinus.animation = AnimationUtils.loadAnimation(
//                    mContext,
//                    R.anim.fade_transition_animation
//                )

            }else{
                binding.customPlusMinus.makeGone()
                binding.btnNotes.makeGone()
                binding.btnOrder.makeVisible()

//                binding.btnNotes.animation = AnimationUtils.loadAnimation(
//                    mContext,
//                    R.anim.translate_right
//                )
//
//                binding.customPlusMinus.animation = AnimationUtils.loadAnimation(
//                    mContext,
//                    R.anim.translate_right
//                )
            }
         }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
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

    interface OrderInterface {
        fun onchange()
    }
}
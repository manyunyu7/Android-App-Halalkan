package com.feylabs.halalkan.view.resto.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tangxiaolv.telegramgallery.Utils.AndroidUtilities.showToast

enum class FoodAdapterType {
    ADMIN, USER_ORDER, USER_REVIEW
}

class RestoFoodAdapter(
    val foodAdapterType: FoodAdapterType = FoodAdapterType.USER_ORDER
) : RecyclerView.Adapter<RestoFoodAdapter.RestaurantHomeViewHolder>() {

    val data = mutableListOf<AdapterModel>()
    lateinit var adapterInterface: ItemInterface
    lateinit var orderInterface: OrderInterface
    lateinit var noteInterface: NoteInterface

    var isRestoClosed  = false

    fun setWithNewData(data: MutableList<AdapterModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    fun setupAdapterInterface(obj: NoteInterface) {
        this.noteInterface = obj
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
                noteInterface.onclick(model, adapterPosition)
            }

            // check if menu is already inserted
            if (orderUtility.isItemAlreadyInserted(model.id.toString())) {
                model.isOrdered = true
                val item = orderUtility.getItem(model.id.toString())
                item?.let {
                    model.notes = it.notes
                    model.orderedQuantity = it.quantity
                }
            } else {
                model.isOrdered = false
            }

            //handle view for ordered items to distinguish order status
            if (model.isOrdered.not()) {
                setToOrdered(false)
            } else {
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
                                    price = model.price.toDouble(),
                                    restoId = model.restoranId,
                                    food = model
                                )
                            )
                        }

                        if (quantity == 0) {
                            setToOrdered(false)
                            orderUtility.removeItem(model.id.toString())

                            if (foodAdapterType == FoodAdapterType.USER_REVIEW) {
                                data.removeAt(adapterPosition)
                                notifyItemRemoved(adapterPosition)
                            }
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

            val existing = orderUtility.getItem(model.id.toString())
            existing?.let {
                if (it.notes.isNotEmpty()) {
                    binding.containerNotes.makeVisible()
                    binding.tvNotes.text = it.notes
                } else {
                    binding.containerNotes.makeGone()
                }
            }

            if (model.isVisible == 1) {
                binding.tvAvailability.text = mContext.getString(R.string.title_available)
                binding.tvAvailability.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.uikit_green_light
                    )
                )
                binding.btnOrder.makeVisible()
                OrderUtility(mContext).removeItem(model.id.toString())
            } else {
                binding.tvAvailability.text = mContext.getString(R.string.title_unavailable)
                binding.tvAvailability.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.uikit_red_light
                    )
                )
                binding.btnOrder.makeGone()
            }

            val userRole = (MyPreference(mContext).getUserRole()?.toIntOrNull() ?: -99)
            if (userRole == 1 || userRole == 3) {
                binding.btnOrder.makeGone()
                binding.labelNotes.makeGone()
                binding.tvNotes.text = model.categoryName.toString() + " - " + model.typeFoodName
            }
        }

        private fun setToOrdered(b: Boolean) {
            if (b) {
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

            } else {
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

    interface NoteInterface {
        fun onclick(
            model: com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse,
            position: Int
        )
    }

    interface OrderInterface {
        fun onchange()
    }
}
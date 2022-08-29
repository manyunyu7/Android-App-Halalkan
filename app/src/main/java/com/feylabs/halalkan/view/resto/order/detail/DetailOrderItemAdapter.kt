package com.feylabs.halalkan.view.resto.order.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.CustomPlusMinus
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.reqres.order.OrderDataModel as AdapterModel
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.CommonUtil.makeVisible
import com.feylabs.halalkan.databinding.ItemFoodBinding as AdapterBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.StringUtil.orMuskoEmpty
import com.feylabs.halalkan.utils.resto.OrderLocalModel
import com.feylabs.halalkan.utils.resto.OrderUtility
import com.tangxiaolv.telegramgallery.Utils.AndroidUtilities.showToast

enum class DetailOrderAdapterType{
    ADMIN,USER_ORDER,USER_REVIEW
}

class DetailOrderItemAdapter(
    val foodAdapterType: DetailOrderAdapterType = DetailOrderAdapterType.USER_ORDER
) : RecyclerView.Adapter<DetailOrderItemAdapter. AdapterViewHolder>() {

    val data = mutableListOf<AdapterModel>()
    lateinit var adapterInterface: ItemInterface
    lateinit var orderInterface: OrderInterface
    lateinit var noteInterface: NoteInterface

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

    inner class  AdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: AdapterBinding = AdapterBinding.bind(itemView)
        var mContext = binding.root.context

        fun onBInd(model: AdapterModel) {
            val mContext = binding.root.context

            if (MyPreference(mContext).getUserRole().toString() != "2") {
                binding.btnOrder.visibility = View.GONE
            }

            binding.root.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.fade_transition_animation
            )

            if (::adapterInterface.isInitialized)
                binding.root.setOnClickListener {
                    adapterInterface.onclick(model)
                }

            binding.tvTitle.text = model.food +"\n(x${model.quantity} pcs )"
            binding.tvPrice.text = model.foodPrice.toString()
            binding.tvNotes.text= model.notes.orMuskoEmpty("-")
            binding.tvFoodDesc.makeGone()
            binding.customPlusMinus.makeGone()
            binding.btnNotes.makeGone()
            binding.btnOrder.makeGone()
            binding.ivMainImage.loadImageFromURL(mContext, model.foodImage)
        }

        private fun setToOrdered(b: Boolean) {
            if (b) {
                binding.btnNotes.makeGone()
                binding.customPlusMinus.makeVisible()
                binding.btnNotes.makeVisible()
                binding.btnOrder.makeGone()
            } else {
                binding.customPlusMinus.makeGone()
                binding.btnNotes.makeGone()
                binding.btnOrder.makeVisible()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  AdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return  AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder:  AdapterViewHolder, position: Int) {
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
            model: com.feylabs.halalkan.data.remote.reqres.order.OrderDataModel,
            position: Int
        )
    }

    interface OrderInterface {
        fun onchange()
    }
}
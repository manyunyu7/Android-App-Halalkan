package com.feylabs.halalkan.view.translate.convo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.utils.StringUtil.prettyPrint
import com.feylabs.halalkan.view.translate.convo.ConvoModel  as AdapterModel
import com.feylabs.halalkan.databinding.ItemChatBinding as AdapterBinding

class ConvoAdapter : RecyclerView.Adapter<ConvoAdapter.AdapterViewHolder>() {

    val data = mutableListOf<AdapterModel>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<AdapterModel>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun addData(data: AdapterModel) {
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

        var binding: AdapterBinding = AdapterBinding.bind(itemView)

        fun onBind(model: AdapterModel) {
            val mContext = binding.root.context



            binding.debugging.visibility = View.GONE
            binding.debugging.text = model.toString().prettyPrint()


            var spelling = model.spelling.orEmpty()
            model.spelling.let {
                if (it == null) {
                    spelling = ""
                } else {
                    if (it.isEmpty()) {
                        spelling = ""
                    } else {
                        spelling = "\n" + spelling
                    }
                }
            }

            if (model.type == "1") {
                //left
                binding.base.animation = AnimationUtils.loadAnimation(
                    mContext,
                    R.anim.translate_left
                )

                binding.containerBubbleChattarget.visibility = View.GONE
                binding.containerBubbleChatsource.visibility = View.VISIBLE

                binding.tvOriginalTextSource.text = model.text
                binding.tvTranslatedSource.text = "${model.result} $spelling"


            } else {
                binding.base.animation = AnimationUtils.loadAnimation(
                    mContext,
                    R.anim.translate_right
                )

                binding.containerBubbleChattarget.visibility = View.VISIBLE
                binding.containerBubbleChatsource.visibility = View.GONE

                binding.tvOriginalTextTarget.text = model.text
                binding.tvTranslatedTarget.text = "${model.result} $spelling"
            }

            binding.tvTimeSource.text = model.from + "-" + model.to
            binding.tvTimeTarget.text = model.from + "-" + model.to

            arrayOf(binding.imgSpeakLeft, binding.imgSpeakRight).forEachIndexed { index, btnSpeak ->
                btnSpeak.setOnClickListener {
                    if (::adapterInterface.isInitialized) {
                        adapterInterface.speak(model)
                    }
                }
            }

            if (::adapterInterface.isInitialized) {

                adapterInterface.translate(model, binding)

                binding.root.setOnClickListener {
                    adapterInterface.speak(model)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun translate(
            model: AdapterModel,
            binding: AdapterBinding
        )

        fun speak(model: AdapterModel)
    }
}
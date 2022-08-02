package com.feylabs.halalkan.view.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.view.prayer.PrayerRoomListUIModel
import com.google.android.material.imageview.ShapeableImageView
import org.w3c.dom.Text

class CategoryListAdapter (private val categoryproductsList:List<CategoryProductsModel>)
    : RecyclerView.Adapter<CategoryListAdapter.CategoryProductsViewHolder>() {

    lateinit var adapterInterface: CategoryListAdapter.ItemInterface

    class CategoryProductsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        val img_category : ImageView = itemView.findViewById(R.id.img)
        val product_category : TextView = itemView.findViewById(R.id.heading)
        val base : CardView = itemView.findViewById(R.id.base)
    }

    fun setupAdapterInterface(obj: CategoryListAdapter.ItemInterface) {
        this.adapterInterface = obj
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_product,parent,false)
        return CategoryProductsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryProductsViewHolder, position: Int) {

        val categoryproductsList = categoryproductsList[position]
        holder.img_category.setImageResource(categoryproductsList.img_category)
        holder.product_category.text = categoryproductsList.product_category
        holder.base.setOnClickListener {
            adapterInterface.onclick(categoryproductsList)
        }

    }

    override fun getItemCount(): Int {
        return categoryproductsList.size
    }

    interface ItemInterface {
        fun onclick(model: CategoryProductsModel)
    }

}
package com.feylabs.halalkan.view.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.google.android.material.imageview.ShapeableImageView
import org.w3c.dom.Text

class CategoryProductAdapter (private val categoryproductsList:List<CategoryProductsModel>)
    : RecyclerView.Adapter<CategoryProductAdapter.CategoryProductsViewHolder>() {


    class CategoryProductsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        val img_category : ShapeableImageView = itemView.findViewById(R.id.image)
        val product_category : TextView = itemView.findViewById(R.id.heading)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_product,parent,false)
        return CategoryProductsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryProductsViewHolder, position: Int) {

        val categoryproductsList = categoryproductsList[position]
        holder.img_category.setImageResource(categoryproductsList.img_category)
        holder.product_category.text = categoryproductsList.product_category
    }

    override fun getItemCount(): Int {
        return categoryproductsList.size
    }

}
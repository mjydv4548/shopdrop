package com.example.shopdrop.presentation.product_list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopdrop.R
import com.example.shopdrop.common.GlideApp
import com.example.shopdrop.domain.model.Products
import com.google.firebase.storage.StorageReference

class ProductAdapter(
    private val listener: OnProductClick,
    private val context: Context,
    private val productList: List<Products>,
    private val reference: StorageReference,
    private val isWishlist: OnWishlistClick
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.description.text = productList[position].description
        holder.price.text = "$${productList[position].price}"
        holder.brand.text = productList[position].brand
        GlideApp.with(context)
            .load(reference.child("${productList[position].images}/image1.jpg"))
            .into(holder.imageView)
        holder.imageView.setOnClickListener {
            listener.onClick(
                productList[position].productId,
                productList[position].description,
                productList[position].price,
                productList[position].isWishListed
            )
        }
        holder.wishlist.isChecked = productList[position].isWishListed

        holder.wishlist.setOnClickListener {
            isWishlist.onWishlistClick(
                productList[position].productId,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val price: TextView = itemView.findViewById(R.id.item_price)
        val description: TextView = itemView.findViewById(R.id.item_description)
        val brand: TextView = itemView.findViewById(R.id.item_brand)
        val wishlist: CheckBox = itemView.findViewById(R.id.item_wishlist)
    }

    interface OnProductClick {
        fun onClick(
            productId: String,
            productDescription: String,
            productPrice: Int,
            isWishListed: Boolean
        )
    }

    interface OnWishlistClick {
        fun onWishlistClick(productId: String, index: Int)
    }


}
package com.example.shopdrop.presentation.user_wishlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopdrop.R
import com.example.shopdrop.common.GlideApp
import com.example.shopdrop.domain.model.Wishlist
import com.google.firebase.storage.StorageReference

class WishListAdapter(
    private val listener: MoveToCartClickListener,
    private val context: Context,
    private val wishlistList: MutableList<Wishlist>,
    private val reference: StorageReference,
    private val removeFromWishlist: RemoveFromWishlist
) : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wishlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = wishlistList[position]
        GlideApp.with(context).load(reference.child("${item.images}/image1.jpg")).into(holder.image)
        holder.description.text = item.description
        holder.price.text = "$${item.price}"
        holder.moveToCart.setOnClickListener {
            listener.onClick(position, item.price,item.productId)
        }
        holder.removeFromWishlist.setOnClickListener {
            removeFromWishlist.removeFromWishlist(item.productId)
        }

    }

    override fun getItemCount(): Int {
        return wishlistList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moveToCart: Button = itemView.findViewById(R.id.btn_wishlist_move_to_cart)
        val image: ImageView = itemView.findViewById(R.id.wishlist_item_image)
        val price: TextView = itemView.findViewById(R.id.wishlist_item_price)
        val description: TextView = itemView.findViewById(R.id.wishlist_item_description)
        val removeFromWishlist: ImageView =
            itemView.findViewById(R.id.btn_wishlist_remove_from_wishlist)
    }

    interface MoveToCartClickListener {
        fun onClick(position: Int, price: Int,productId: String)
    }

    interface RemoveFromWishlist {
        fun removeFromWishlist(productId: String)
    }

}
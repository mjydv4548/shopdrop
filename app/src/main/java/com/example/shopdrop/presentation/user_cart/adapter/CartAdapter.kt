package com.example.shopdrop.presentation.user_cart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants
import com.example.shopdrop.common.GlideApp
import com.example.shopdrop.presentation.user_cart.model.CartItem
import com.google.firebase.storage.StorageReference

class CartAdapter(
    private val context: Context,
    private val cartList: List<CartItem>,
    private val reference: StorageReference,
    private val changeQuantity: ChangeQuantity
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cartList[position]
        GlideApp.with(context).load(reference.child("${item.image}/image1.jpg")).into(holder.image)
        holder.description.text = item.description
        holder.quantity.text = item.quantity.toString()
        holder.price.text = "₹ ${item.price}"
        holder.productSize.text = item.selectedSize

        holder.increaseQuantity.setOnClickListener {
            changeQuantity.changeQuantity(
                Constants.INCREASE,
                item.productId,
                position,
                item.selectedSize
            )
        }
        holder.decreaseQuantity.setOnClickListener {
            changeQuantity.changeQuantity(
                Constants.DECREASE,
                item.productId,
                position,
                item.selectedSize
            )
        }

    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.cart_item_image)
        val price: TextView = itemView.findViewById(R.id.cart_item_price)
        val description: TextView = itemView.findViewById(R.id.cart_item_description)
        val productSize: TextView = itemView.findViewById(R.id.cart_item_size)
        val quantity: TextView = itemView.findViewById(R.id.cart_item_quantity)
        val increaseQuantity: ImageView = itemView.findViewById(R.id.cart_increase)
        val decreaseQuantity: ImageView = itemView.findViewById(R.id.cart_decrease)

    }

    interface ChangeQuantity {
        fun changeQuantity(action: String, productId: String, index: Int, selectedSize: String)
    }
}
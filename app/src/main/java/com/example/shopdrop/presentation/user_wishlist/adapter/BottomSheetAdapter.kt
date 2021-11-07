package com.example.shopdrop.presentation.user_wishlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopdrop.R
import com.example.shopdrop.presentation.user_wishlist.model.Size

class BottomSheetAdapter(
    private val context: Context,
    private val sizeList: Size,
    private val listener: SelectedSize
) :
    RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_size, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.currentSize.text = sizeList.list[position]
        holder.itemView.setOnClickListener {
            sizeList.selectedSize = sizeList.list[position]
            notifyDataSetChanged()
        }

        if (sizeList.selectedSize == sizeList.list[position]) {
            holder.currentSize.setBackgroundResource(R.drawable.size_selected_bg)
            listener.selectedSize(sizeList.selectedSize)
        } else {
            holder.currentSize.setBackgroundResource(R.drawable.size_present_bg)
        }
    }

    override fun getItemCount(): Int {
        return sizeList.list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currentSize: TextView = itemView.findViewById(R.id.item_size)
    }

    interface SelectedSize {
        fun selectedSize(selectedSize: String)
    }

}
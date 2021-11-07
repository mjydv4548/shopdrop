package com.example.shopdrop.presentation.update_address.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopdrop.R

class AddressAdapter(private val context: Context) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 3
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txt_item_address_name)
        val streetAddress: TextView = itemView.findViewById(R.id.txt_item_street_address)
        val locality: TextView = itemView.findViewById(R.id.txt_item_locality)
        val city: TextView = itemView.findViewById(R.id.txt_item_address_city)
        val state: TextView = itemView.findViewById(R.id.txt_item_address_state)
        val zipCode: TextView = itemView.findViewById(R.id.txt_item_zip_code)
        val phone: TextView = itemView.findViewById(R.id.txt_item_phone)
        val buttonRemove: Button = itemView.findViewById(R.id.btn_remove_address)
        val buttonEdit: Button = itemView.findViewById(R.id.btn_edit_address)

    }

}
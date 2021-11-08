package com.example.shopdrop.presentation.update_address.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopdrop.R
import com.example.shopdrop.data.model.UserAddressDto

class AddressAdapter(
    private val context: Context,
    private val listener: EditClickHandler,
    private val addressList: MutableList<UserAddressDto>,
    private val handler: RemoveClickHandler
) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentAddress = addressList[position]
        holder.name.text = currentAddress.name
        holder.streetAddress.text = currentAddress.streetAddress
        holder.locality.text = currentAddress.locality
        holder.city.text = currentAddress.city
        holder.state.text = currentAddress.state
        holder.zipCode.text = currentAddress.zipCode.toString()
        holder.phone.text = currentAddress.phone.toString()
        holder.buttonEdit.setOnClickListener {
            listener.edit(position)
        }
        holder.buttonRemove.setOnClickListener {
            handler.remove(position)
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txt_item_address_name)
        val streetAddress: TextView = itemView.findViewById(R.id.txt_item_street_address)
        val locality: TextView = itemView.findViewById(R.id.txt_item_locality)
        val city: TextView = itemView.findViewById(R.id.txt_item_address_city)
        val state: TextView = itemView.findViewById(R.id.txt_item_address_state)
        val zipCode: TextView = itemView.findViewById(R.id.txt_item_zip_code)
        val phone: TextView = itemView.findViewById(R.id.txt_item_phone)
        val buttonRemove: TextView = itemView.findViewById(R.id.btn_remove_address)
        val buttonEdit: TextView = itemView.findViewById(R.id.btn_edit_address)

    }

    interface EditClickHandler {
        fun edit(index: Int)
    }

    interface RemoveClickHandler {
        fun remove(index: Int)
    }

}
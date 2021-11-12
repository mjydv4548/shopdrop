package com.example.shopdrop.presentation.user_order.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopdrop.R
import com.example.shopdrop.data.model.UserOrderDto

class OrdersAdapter(
    private val context: Context,
    private val orderList: List<UserOrderDto>,
    private val orderTracking: TrackOrder,
    private val getDetails: OrderDetails
) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentOrder = orderList[position]
        holder.orderId.text = "ORDER# ${currentOrder.orderId}"
        holder.totalPrice.text = "₹${currentOrder.orderTotalPrice}"
        holder.orderQuantity.text = currentOrder.orderQuantity.toString()
        if (currentOrder.cancelled) {
            holder.orderState.text = context.getString(R.string.order_cancelled)
            holder.orderState.setBackgroundResource(R.drawable.txt_order_cancelled_bg)
        }
        if (currentOrder.completed) {
            holder.orderState.text = context.getString(R.string.order_completed)
            holder.orderState.setBackgroundResource(R.drawable.txt_order_completed_bg)
        }
        if (currentOrder.onGoing) {
            holder.orderState.text = context.getString(R.string.order_on_going)
            holder.orderState.setBackgroundResource(R.drawable.txt_order_ongoing_bg)
        }

        holder.track.setOnClickListener {
            orderTracking.trackOrder(position)
        }

        holder.details.setOnClickListener {
            getDetails.getOrderDetails(position)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId: TextView = itemView.findViewById(R.id.txt_orderID)
        val orderState: TextView = itemView.findViewById(R.id.txt_order_state)
        val orderQuantity: TextView = itemView.findViewById(R.id.txt_quantity)
        val totalPrice: TextView = itemView.findViewById(R.id.txt_total_price)
        val track: Button = itemView.findViewById(R.id.btn_track_order)
        val details: Button = itemView.findViewById(R.id.btn_details)
    }

    interface TrackOrder {
        fun trackOrder(index: Int)
    }

    interface OrderDetails {
        fun getOrderDetails(index: Int)
    }
}
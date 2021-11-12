package com.example.shopdrop.presentation.user_order

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.shopdrop.R
import com.example.shopdrop.data.model.UserOrderDto
import kotlinx.android.synthetic.main.fragment_track_order.*


class OrderTrackingFragment : Fragment(R.layout.fragment_track_order) {

    private val args: OrderTrackingFragmentArgs by navArgs()
    private lateinit var orderDetails: UserOrderDto
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txt_tracking_orderId.text = "Order Id # ${args.orderDetails.orderId}"

        orderDetails = args.orderDetails

        val currentProgress = orderDetails.orderTracking

        if (!orderDetails.cancelled) {
            if (currentProgress.orderConfirmed) {
                iv_order_confirmed.setImageResource(R.drawable.order_tracking_success)
                line_order_confirmed.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
            }

            if (currentProgress.orderShipped) {
                iv_order_shipped.setImageResource(R.drawable.order_tracking_success)
                line_order_shipped.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
            }

            if (currentProgress.orderProgress) {
                iv_order_progress.setImageResource(R.drawable.order_tracking_success)
                line_order_progress.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
            }

            if (currentProgress.orderDelivered) {
                iv_order_delivered.setImageResource(R.drawable.order_tracking_success)
                line_order_delivered.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
            }

        } else {
            hideViews()

            if (currentProgress.orderConfirmed) {
                iv_order_confirmed.setImageResource(R.drawable.order_tracking_success)
                line_order_confirmed.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
                iv_order_confirmed.isVisible = true
                line_order_confirmed.isVisible = true
                cl_order_confirmed.isVisible = true
            } else {
                iv_order_confirmed.setImageResource(R.drawable.order_tracking_cancelled)
                line_order_confirmed.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
                imageConfirmed.setImageResource(R.drawable.ic_cancelled)
                textOrderConfirmed.text = "Order Cancelled"
                textOrderConfirmedDes.text = "Your order is cancelled"
                iv_order_confirmed.isVisible = true
                line_order_confirmed.isVisible = true
                cl_order_confirmed.isVisible = true
                return
            }


            if (currentProgress.orderShipped) {
                iv_order_shipped.setImageResource(R.drawable.order_tracking_success)
                line_order_shipped.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
                iv_order_shipped.isVisible = true
                line_order_shipped.isVisible = true
                cl_order_shipped.isVisible = true
            } else {
                iv_order_shipped.setImageResource(R.drawable.order_tracking_cancelled)
                line_order_shipped.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
                imageShipped.setImageResource(R.drawable.ic_cancelled)
                textOrderShipped.text = "Order Cancelled"
                textOrderShippedDes.text = "Your order is cancelled"
                iv_order_shipped.isVisible = true
                line_order_shipped.isVisible = true
                cl_order_shipped.isVisible = true
                return

            }


            if (currentProgress.orderProgress) {
                iv_order_progress.setImageResource(R.drawable.order_tracking_success)
                line_order_progress.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
                iv_order_progress.isVisible = true
                line_order_progress.isVisible = true
                cl_order_progress.isVisible = true
            } else {
                iv_order_progress.setImageResource(R.drawable.order_tracking_cancelled)
                line_order_progress.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
                imageProgress.setImageResource(R.drawable.ic_cancelled)
                textOrderProgress.text = "Order Cancelled"
                textOrderProgressDes.text = "Your order is cancelled"
                iv_order_progress.isVisible = true
                line_order_progress.isVisible = true
                cl_order_progress.isVisible = true
                return
            }

            if (currentProgress.orderDelivered) {
                iv_order_delivered.setImageResource(R.drawable.order_tracking_success)
                line_order_delivered.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
                iv_order_delivered.isVisible = true
                line_order_delivered.isVisible = true
                cl_order_delivered.isVisible = true
            } else {
                iv_order_delivered.setImageResource(R.drawable.order_tracking_cancelled)
                line_order_delivered.setBackgroundColor(requireActivity().resources.getColor(R.color.colorGreen))
                imageDelivered.setImageResource(R.drawable.ic_cancelled)
                textOrderDelivered.text = "Order Cancelled"
                textOrderDeliveredDes.text = "Your order is cancelled"
                iv_order_delivered.isVisible = true
                line_order_delivered.isVisible = true
                cl_order_delivered.isVisible = true
                return
            }

        }
    }


    private fun hideViews() {
        iv_order_confirmed.isVisible = false
        line_order_confirmed.isVisible = false
        cl_order_confirmed.isVisible = false


        iv_order_shipped.isVisible = false
        line_order_shipped.isVisible = false
        cl_order_shipped.isVisible = false


        iv_order_progress.isVisible = false
        line_order_progress.isVisible = false
        cl_order_progress.isVisible = false


        iv_order_delivered.isVisible = false
        line_order_delivered.isVisible = false
        cl_order_delivered.isVisible = false
    }
}
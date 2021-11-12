package com.example.shopdrop.presentation.user_order

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopdrop.R
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.OrderDetails
import com.example.shopdrop.data.model.OrderTracking
import com.example.shopdrop.data.model.UserOrderDto
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import com.example.shopdrop.presentation.user_order.view_model.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_order_successful.*

@AndroidEntryPoint
class OrderSuccessfulFragment : Fragment(R.layout.fragment_order_successful) {

    private val args: OrderSuccessfulFragmentArgs by navArgs()
    private val userViewModel: UserViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = userViewModel.getCurrentUser()!!.uid
        val itemList = args.orderDetails.toList()

        val orderList: MutableList<UserOrderDto> = mutableListOf()

        for (order in itemList.indices) {
            val orderId = System.currentTimeMillis() * (order + 1)
            val orderTotalPrice = itemList[order].price * itemList[order].quantity
            val orderQuantity = itemList[order].quantity
            val orderTracking = OrderTracking(orderPlaced = true)
            val orderDetails =
                OrderDetails(
                    itemList[order].productBrand,
                    itemList[order].image,
                    itemList[order].description,
                    itemList[order].selectedSize
                )
            val deliveryAddress = args.deliveryAddress

            val orderDto = UserOrderDto(
                orderId.toString(),
                orderTotalPrice,
                orderQuantity,
                onGoing = true,
                orderTracking = orderTracking,
                orderDetails = orderDetails,
                deliveryAddress = deliveryAddress
            )
            orderList.add(orderDto)
        }


        orderViewModel.addOrders(currentUser, orderList)
        orderViewModel.addOrders.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    payment_successful_progress.isVisible = true
                }
                is Resource.Success -> {
                    payment_successful_progress.isVisible = false
                    Toast.makeText(requireContext(), "Order Placed", Toast.LENGTH_LONG).show()
                }
                is Resource.Error -> {
                    payment_successful_progress.isVisible = false
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        })

        btn_back_to_home.setOnClickListener {
            val action =
                OrderSuccessfulFragmentDirections.actionOrderSuccessfulFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}
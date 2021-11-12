package com.example.shopdrop.presentation.user_order

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopdrop.R
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserOrderDto
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import com.example.shopdrop.presentation.user_order.adapter.OrdersAdapter
import com.example.shopdrop.presentation.user_order.view_model.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_orders.*

@AndroidEntryPoint
class OrdersFragment : Fragment(R.layout.fragment_orders), OrdersAdapter.TrackOrder,
    OrdersAdapter.OrderDetails {

    private val orderViewModel: OrderViewModel by viewModels()
    private lateinit var ordersAdapter: OrdersAdapter
    private val userViewModel: UserViewModel by activityViewModels()
    private val ordersList: MutableList<UserOrderDto> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = userViewModel.getCurrentUser()!!.uid

        ordersAdapter = OrdersAdapter(requireActivity(), ordersList, this, this)

        orders_recycleView.layoutManager = LinearLayoutManager(context)
        orders_recycleView.adapter = ordersAdapter

        orderViewModel.getUserOrders(currentUser)
        orderViewModel.userOrders.observe(viewLifecycleOwner, Observer { order ->
            when (order) {
                is Resource.Loading -> {
                    orders_progress.isVisible = true
                }
                is Resource.Success -> {
                    orders_progress.isVisible = false
                    ordersList.clear()
                    ordersList.addAll(order.data!!)
                    ordersAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    orders_progress.isVisible = false
                }
            }
        })

        btn_orders_back_to_home.setOnClickListener {
            val action = OrdersFragmentDirections.actionOrdersFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    override fun trackOrder(index: Int) {
        val action =
            OrdersFragmentDirections.actionOrdersFragmentToOrderTrackingFragment(ordersList[index])
        findNavController().navigate(action)
    }

    override fun getOrderDetails(index: Int) {
        val action =
            OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(ordersList[index])
        findNavController().navigate(action)
    }
}
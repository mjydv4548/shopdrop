package com.example.shopdrop.presentation.user_cart

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants
import com.example.shopdrop.common.Resource
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import com.example.shopdrop.presentation.user_cart.adapter.CartAdapter
import com.example.shopdrop.presentation.user_cart.model.CartItem
import com.example.shopdrop.presentation.user_cart.view_model.CartViewModel
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart), CartAdapter.ChangeQuantity {
    @Inject
    lateinit var reference: StorageReference
    private val cartList: MutableList<CartItem> = mutableListOf()
    private lateinit var cartAdapter: CartAdapter
    private val cartViewModel: CartViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(Constants.LOGIN_SUCCESSFUL)
            .observe(currentBackStackEntry, Observer { success ->
                if (!success) {
                    val startDestination = navController.graph.startDestination
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(startDestination, true)
                        .build()

                    navController.navigate(startDestination, null, navOptions)
                }
            })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (cartList.size == 0) {
            hide_cart.isVisible = false
            empty_cart.isVisible = true
        } else {
            hide_cart.isVisible = true
            empty_cart.isVisible = false
        }

        val user = userViewModel.getCurrentUser()
        if (user != null) {
            cartAdapter = CartAdapter(requireActivity(), cartList, reference, this)
            cart_recycle_view.layoutManager = LinearLayoutManager(context)
            cart_recycle_view.adapter = cartAdapter

            cartViewModel.getCart(user.uid)
            cartViewModel.cart.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Loading -> {
                        cart_progress.isVisible = true
                    }
                    is Resource.Success -> {

                        val list = it.data
                        cartList.clear()
                        cartList.addAll(list!!)

                        var subTotal = 0
                        for (item in list) {
                            subTotal += item.price * item.quantity
                        }
                        val shippingCost = list.size * 20
                        val total = subTotal + shippingCost
                        txt_subtotal.text = "$$subTotal"
                        txt_shippingCost.text = "$$shippingCost"
                        txt_total.text = "$$total"
                        cartAdapter.notifyDataSetChanged()

                        cart_progress.isVisible = false
                        if (cartList.size == 0) {
                            hide_cart.isVisible = false
                            empty_cart.isVisible = true
                        } else {
                            hide_cart.isVisible = true
                            empty_cart.isVisible = false
                        }


                    }
                    is Resource.Error -> {
                        cart_progress.isVisible = false
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> Unit
                }
            })

        } else {
            val action = CartFragmentDirections.actionCartFragmentToLoginFragment()
            findNavController().navigate(action)
        }


    }

    override fun changeQuantity(
        action: String,
        productId: String,
        index: Int,
        selectedSize: String
    ) {
        val userId = userViewModel.getCurrentUser()!!.uid
        if (action == Constants.INCREASE) {
            lifecycleScope.launch {
                lifecycleScope.async {
                    cartViewModel.updateCart(userId, productId, selectedSize, Constants.INCREASE)
                }.await()
                cartViewModel.getCart(userId)
            }

        } else {
            lifecycleScope.launch {
                lifecycleScope.async {
                    cartViewModel.updateCart(userId, productId, selectedSize, Constants.DECREASE)
                }.await()
                cartViewModel.getCart(userId)
            }


        }
    }


}
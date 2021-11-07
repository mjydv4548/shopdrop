package com.example.shopdrop.presentation.user_wishlist

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
import com.example.shopdrop.domain.model.Wishlist
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import com.example.shopdrop.presentation.user_wishlist.adapter.WishListAdapter
import com.example.shopdrop.presentation.user_wishlist.view_model.UserWishlistViewModel
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_wishlist.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WishListFragment : Fragment(R.layout.fragment_wishlist),

    WishListAdapter.MoveToCartClickListener, WishListAdapter.RemoveFromWishlist {
    @Inject
    lateinit var reference: StorageReference
    private val wishlist: MutableList<Wishlist> = mutableListOf()
    private lateinit var wishlistAdapter: WishListAdapter
    private var currentProductId: String = ""
    private val userViewModel: UserViewModel by activityViewModels()
    private val userWishlistViewModel: UserWishlistViewModel by activityViewModels()

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

        savedStateHandle.getLiveData<String>(Constants.SELECTED_SIZE)
            .observe(currentBackStackEntry, Observer { selectedSize ->
                if (selectedSize.isNotEmpty()) {
                    val user = userViewModel.getCurrentUser()
                    if (user != null) {
                        lifecycleScope.launch {
                            val response =
                                userWishlistViewModel.updateCart(
                                    user.uid,
                                    currentProductId,
                                    selectedSize,
                                    Constants.INCREASE
                                )
                            when (response) {
                                is Resource.Success -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Added to Cart",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                is Resource.Error -> {
                                    Toast.makeText(
                                        requireContext(),
                                        response.errorMessage,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                                else -> Unit
                            }
                        }

                    }
                }
            })


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        empty_wishlist.isVisible = wishlist.size == 0

        val user = userViewModel.getCurrentUser()
        if (user != null) {
            wishlistAdapter = WishListAdapter(
                this, requireActivity(), wishlist, reference, this
            )
            wishlist_recycle_view.layoutManager = LinearLayoutManager(context)
            wishlist_recycle_view.adapter = wishlistAdapter

            userWishlistViewModel.getWishList(user.uid)
            userWishlistViewModel.wishlist.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Loading -> {
                        wishlist_progress.isVisible = true
                    }
                    is Resource.Success -> {
                        wishlist_progress.isVisible = false
                        wishlist.clear()
                        wishlist.addAll(it.data!!)
                        wishlistAdapter.notifyDataSetChanged()

                        empty_wishlist.isVisible = wishlist.size == 0
                    }
                    is Resource.Error -> {
                        wishlist_progress.isVisible = false
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })

        } else {
            val action = WishListFragmentDirections.actionWishListFragmentToLoginFragment()
            navController.navigate(action)
        }

    }

    override fun onClick(position: Int, price: Int, productId: String) {
        val list = wishlist[position].productAvailableSize
        currentProductId = productId
        val sizeList: Array<String> = list.toTypedArray()
        val action = WishListFragmentDirections.actionWishListFragmentToSizeSelectionFragment(
            sizeList,
            price,
            productId
        )
        findNavController().navigate(action)
    }

    override fun removeFromWishlist(productId: String) {
        val user = userViewModel.getCurrentUser()!!.uid
        lifecycleScope.launch {
            when (val result = userWishlistViewModel.updateWishlist(user, productId)) {
                is Resource.Success -> {
                    userWishlistViewModel.getWishList(user)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), result.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }


    }


}
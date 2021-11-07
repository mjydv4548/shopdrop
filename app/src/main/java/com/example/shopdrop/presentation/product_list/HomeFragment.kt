package com.example.shopdrop.presentation.product_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopdrop.R
import com.example.shopdrop.common.Resource
import com.example.shopdrop.domain.model.Products
import com.example.shopdrop.presentation.product_list.adapter.ProductAdapter
import com.example.shopdrop.presentation.product_list.view_model.ProductsViewModel
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), ProductAdapter.OnProductClick,
    ProductAdapter.OnWishlistClick {
    @Inject
    lateinit var reference: StorageReference
    private val productList: MutableList<Products> = mutableListOf()
    private lateinit var adapter: ProductAdapter
    private val userViewModel: UserViewModel by activityViewModels()
    private val productsViewModel: ProductsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val user = userViewModel.getCurrentUser()
        if (user != null) {
            productsViewModel.getProducts(user.uid)

        } else {
            productsViewModel.getProducts(null)
        }


        productsViewModel.productList.observe(viewLifecycleOwner, Observer { product ->
            when (product) {
                is Resource.Loading -> {
                    home_progress_bar.isVisible = true
                }
                is Resource.Success -> {
                    home_progress_bar.isVisible = false
                    productList.clear()
                    productList.addAll(product.data!!)
                    adapter.notifyDataSetChanged()

                }
                is Resource.Error -> {
                    home_progress_bar.isVisible = false
                    Toast.makeText(requireContext(), product.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    home_progress_bar.isVisible = false
                    Toast.makeText(requireContext(), "Unknown Error Occurred", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        adapter = ProductAdapter(this, requireActivity(), productList, reference, this)
        home_recycleView.layoutManager = GridLayoutManager(context, 2)
        home_recycleView.adapter = adapter


    }


    override fun onClick(
        productId: String,
        productDescription: String,
        productPrice: Int,
        isWishListed: Boolean
    ) {
        val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(
            productId,
            productDescription,
            productPrice,
            isWishListed
        )
        findNavController().navigate(action)
    }

    override fun onWishlistClick(productId: String, index: Int) {
        val user = userViewModel.getCurrentUser()
        if (user != null) {
            lifecycleScope.launch {
                when (val result = productsViewModel.updateWishlist(user.uid, productId)) {
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            result.errorMessage,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    else -> Unit
                }
            }

        } else {
            val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }
}
package com.example.shopdrop.presentation.product_details

import android.content.Intent
import android.graphics.Color
import android.net.Uri
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants
import com.example.shopdrop.common.Resource
import com.example.shopdrop.domain.model.Review
import com.example.shopdrop.presentation.product_details.adapter.ReviewAdapter
import com.example.shopdrop.presentation.product_details.adapter.SizeAdapter
import com.example.shopdrop.presentation.product_details.adapter.SliderAdapter
import com.example.shopdrop.presentation.product_details.model.SliderItem
import com.example.shopdrop.presentation.product_details.view_model.ProductDetailsViewModel
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import com.example.shopdrop.presentation.user_wishlist.model.Size
import com.google.firebase.storage.StorageReference
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product), SizeAdapter.SelectedSize {

    @Inject
    lateinit var reference: StorageReference
    private var slider = SliderItem()
    private val reviewList: MutableList<Review> = mutableListOf()
    private var sizeList = Size()
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var sizeAdapter: SizeAdapter
    private lateinit var sliderView: SliderView
    private val args: ProductDetailsFragmentArgs by navArgs()
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var sizeUrl: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sizeAdapter = SizeAdapter(requireActivity(), sizeList, this)

        sliderAdapter = SliderAdapter(requireActivity(), slider, reference)
        reviewAdapter = ReviewAdapter(requireActivity(), reviewList, reference)
        sliderView = view.findViewById(R.id.product_image_slider)
        sliderView.setSliderAdapter(sliderAdapter)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = Color.GRAY
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = false
        product_review_recycleView.layoutManager = LinearLayoutManager(context)
        product_review_recycleView.adapter = reviewAdapter

        btn_product_wishlist.isChecked = args.isWishListed

        size_recycle_view.layoutManager = GridLayoutManager(context, 3)
        size_recycle_view.adapter = sizeAdapter

        product_description.text = args.productDescription
        product_price.text = "₹ ${args.productPrice}"

        btn_product_detail.setOnCheckedChangeListener { _, isChecked ->
            product_details.isVisible = isChecked
        }

        btn_product_size.setOnCheckedChangeListener { _, isChecked ->
            product_fit_size.isVisible = isChecked
        }

        btn_product_material.setOnCheckedChangeListener { _, isChecked ->
            product_material_care.isVisible = isChecked
        }

        product_fit_size.setOnClickListener {
            val uri = Uri.parse(sizeUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


        val user = userViewModel.getCurrentUser()
        if (user != null) {
            productDetailsViewModel.getProduct(args.productId, user.uid)
        } else {
            productDetailsViewModel.getProduct(args.productId, null)
        }



        productDetailsViewModel.product.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    val product = it.data
                    slider.imagePath = product!!.productImages
                    slider.size = product.noOfImages
                    sliderAdapter.notifyDataSetChanged()

                    reviewList.clear()
                    reviewList.addAll(product.productReviews)
                    reviewAdapter.notifyDataSetChanged()

                    sizeList.list.clear()
                    sizeList.list.addAll(product.productAvailableSize as MutableList<String>)
                    sizeAdapter.notifyDataSetChanged()

                    product_details.text = product.productDetails.replace("\\n", "\n", false)
                    product_material_care.text =
                        product.productMaterialAndCare.replace("\\n", "\n", false)
                    product_fit_size.text = "Visit ${product.productBrand}"
                    sizeUrl = product.productFitAndSizing

                    btn_product_wishlist.isChecked = product.isWishListed
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })

        btn_product_add_to_cart.setOnClickListener {
            if (sizeList.selectedSize.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select a size to proceed",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val currentUser = userViewModel.getCurrentUser()
                if (currentUser != null) {
                    lifecycleScope.launch {
                        val cartResponse = productDetailsViewModel.updateCart(
                            currentUser.uid,
                            args.productId,
                            sizeList.selectedSize,
                            Constants.INCREASE
                        )

                        when (cartResponse) {
                            is Resource.Success -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Added to Cart",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is Resource.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    cartResponse.errorMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> Unit
                        }
                    }
                } else {
                    val action =
                        ProductDetailsFragmentDirections.actionProductDetailsFragmentToLoginFragment()
                    findNavController().navigate(action)
                }

            }
        }

        btn_product_wishlist.setOnClickListener {
            val currentUser = userViewModel.getCurrentUser()
            if (currentUser != null) {
                lifecycleScope.launch {
                    productDetailsViewModel.updateWishlist(
                        currentUser.uid,
                        args.productId
                    )
                    productDetailsViewModel.getProduct(args.productId, currentUser.uid)
                }

            } else {
                val action =
                    ProductDetailsFragmentDirections.actionProductDetailsFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }

    }

    override fun selectedSize(selectedSize: String) {
        sizeList.selectedSize = selectedSize
    }

}
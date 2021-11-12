package com.example.shopdrop.presentation.user_order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopdrop.R
import com.example.shopdrop.common.GlideApp
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_order_details.*
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailsFragment : Fragment(R.layout.fragment_order_details) {

    @Inject
    lateinit var reference: StorageReference
    private val args: OrderDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()

        btn_details_to_home.setOnClickListener {
            val action = OrderDetailsFragmentDirections.actionOrderDetailsFragmentToHomeFragment()
            findNavController().navigate(action)
        }

    }

    private fun initializeView() {
        GlideApp.with(requireContext())
            .load(reference.child("${args.orderDetails.orderDetails.productImage}/image1.jpg"))
            .into(image_productImage)

        txt_details_product_brand.text = args.orderDetails.orderDetails.productBrand
        txt_details_product_description.text = args.orderDetails.orderDetails.productDescription
        txt_details_ordered_size.text =
            "Size : ${args.orderDetails.orderDetails.productSizeOrdered}"

        txt_details_order_quantity.text = "Quantity : ${args.orderDetails.orderQuantity}"

        txt_total_price.text = "₹ ${args.orderDetails.orderTotalPrice}"

        txt_item_address_name.text = args.orderDetails.deliveryAddress.name
        txt_item_street_address.text = args.orderDetails.deliveryAddress.streetAddress
        txt_item_locality.text = args.orderDetails.deliveryAddress.locality
        txt_item_address_city.text = args.orderDetails.deliveryAddress.city
        txt_item_address_state.text = args.orderDetails.deliveryAddress.state
        txt_item_zip_code.text = args.orderDetails.deliveryAddress.zipCode.toString()
        txt_item_phone.text = args.orderDetails.deliveryAddress.phone.toString()
    }
}
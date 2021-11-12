package com.example.shopdrop.presentation.user_address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserAddressDto
import com.example.shopdrop.data.model.UserProfileDto
import com.example.shopdrop.data.model.filterDefault
import com.example.shopdrop.presentation.razorpay.PaymentActivity
import com.example.shopdrop.presentation.user_address.adapter.CartItemAdapter
import com.example.shopdrop.presentation.user_address.viewModel.AddressViewModel
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import com.example.shopdrop.presentation.user_cart.model.CartItem
import com.example.shopdrop.presentation.user_cart.view_model.CartViewModel
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_select_address.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectAddressFragment : Fragment(R.layout.fragment_select_address) {

    @Inject
    lateinit var reference: StorageReference
    private val args: SelectAddressFragmentArgs by navArgs()
    private val addressViewModel: AddressViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var amount: String
    private lateinit var cartList: MutableList<CartItem>
    private lateinit var profile: UserProfileDto
    private var selectedIndex: String? = null
    private var selectedAddress: UserAddressDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle

        savedStateHandle.getLiveData<String>(Constants.INDEX)
            .observe(currentBackStackEntry, Observer { index ->
                if (index != null) {
                    selectedIndex = index
                }
            })

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val a = result.data!!.getBooleanExtra("response", false)
                    if (a) {
                        lifecycleScope.launch {
                            val user = userViewModel.getCurrentUser()!!.uid
                            cartViewModel.emptyCart(user)
                            val action =
                                SelectAddressFragmentDirections.actionSelectAddressFragmentToOrderSuccessfulFragment(
                                    args.cartList,
                                    selectedAddress!!
                                )
                            findNavController().navigate(action)
                        }
                    } else {
                        val action =
                            SelectAddressFragmentDirections.actionSelectAddressFragmentToOrderFailedFragment()
                        findNavController().navigate(action)
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = userViewModel.getCurrentUser()!!.uid

        if (selectedAddress != null) {
            cardView3.isVisible = true
            setAddress(selectedAddress!!)
        } else {
            cardView3.isVisible = false
        }

        if (args.amount != -1) {
            amount = args.amount.toString()
        }

        if (args.userProfile != null) {
            profile = args.userProfile!!
        }

        cartList = args.cartList.toMutableList()

        address_select_recycleView.layoutManager = LinearLayoutManager(context)
        address_select_recycleView.adapter = CartItemAdapter(requireContext(), cartList, reference)

        addressViewModel.getAddress(currentUser)
        addressViewModel.address.observe(viewLifecycleOwner, Observer { address ->
            when (address) {
                is Resource.Loading -> {
                    select_address_progress.isVisible = true
                }
                is Resource.Success -> {
                    select_address_progress.isVisible = false
                    val addressList = address.data
                    if (addressList != null && addressList.isEmpty()) {
                        selectedAddress = null
                    }
                    if (selectedAddress == null) {
                        val list = addressList?.filter { it.filterDefault() }
                        selectedAddress = if (list != null && list.isNotEmpty()) {
                            list[0]
                        } else {
                            if (addressList != null && addressList.isNotEmpty()) {
                                addressList[0]
                            } else {
                                null
                            }

                        }

                        selectedAddress?.let {
                            cardView3.isVisible = true
                            setAddress(it)
                        }
                    }

                    if (selectedIndex != null) {
                        selectedAddress = addressList?.get(selectedIndex!!.toInt())
                        setAddress(selectedAddress!!)
                    }
                }
                is Resource.Error -> {
                    select_address_progress.isVisible = false
                    Toast.makeText(requireContext(), address.errorMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
        })




        btn_add_or_change_address.setOnClickListener {
            val action =
                SelectAddressFragmentDirections.actionSelectAddressFragmentToAddressFragment(
                    true
                )
            findNavController().navigate(action)
        }

        continue_to_payment.setOnClickListener {
            if (cardView3.isVisible) {
                val intent = Intent(context, PaymentActivity::class.java)
                intent.putExtra("amount", amount)
                intent.putExtra("email", profile.userEmail)
                intent.putExtra("phone", profile.userPhone)

                resultLauncher.launch(intent)
            } else {
                Toast.makeText(requireContext(), "Please add a address", Toast.LENGTH_LONG).show()
            }

        }
    }


    private fun setAddress(selectedAddress: UserAddressDto) {
        txt_item_address_name.text = selectedAddress.name
        txt_item_street_address.text = selectedAddress.streetAddress
        txt_item_locality.text = selectedAddress.locality
        txt_item_address_city.text = selectedAddress.city
        txt_item_address_state.text = selectedAddress.state
        txt_item_zip_code.text = selectedAddress.zipCode.toString()
        txt_item_phone.text = selectedAddress.phone.toString()
    }
}
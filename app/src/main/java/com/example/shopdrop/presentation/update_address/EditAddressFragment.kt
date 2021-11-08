package com.example.shopdrop.presentation.update_address

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserAddressDto
import com.example.shopdrop.presentation.update_address.viewModel.AddressViewModel
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_address.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditAddressFragment : Fragment(R.layout.fragment_edit_address) {

    private val args: EditAddressFragmentArgs by navArgs()
    private val addressViewModel: AddressViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.index == -1) {
            txt_edit_address.text = "Add Address"
        } else {
            txt_edit_address.text = "Edit Address"
        }

        val userAddress = args.addressDetails

        userAddress?.let {
            et_edit_address_Name.setText(userAddress.name)
            et_edit_address_mobile.setText(userAddress.phone.toString())
            et_edit_address_zipcode.setText(userAddress.zipCode.toString())
            et_edit_address_state.setText(userAddress.state)
            et_edit_street_address.setText(userAddress.streetAddress)
            et_edit_address_locality.setText(userAddress.locality)
            et_edit_address_city.setText(userAddress.city)
            edit_address_checkbox.isChecked = userAddress.defaultAddress
        }

        btn_save_address.setOnClickListener {
            val currentUser = userViewModel.getCurrentUser()!!.uid
            if (et_edit_address_Name.text.isNotEmpty() &&
                et_edit_address_mobile.text.isNotEmpty() &&
                et_edit_address_zipcode.text.isNotEmpty() &&
                et_edit_address_state.text.isNotEmpty() &&
                et_edit_street_address.text.isNotEmpty() &&
                et_edit_address_locality.text.isNotEmpty() &&
                et_edit_address_city.text.isNotEmpty()
            ) {
                val address = UserAddressDto(
                    et_edit_address_city.text.toString(),
                    et_edit_address_state.text.toString(),
                    edit_address_checkbox.isChecked,
                    et_edit_street_address.text.toString(),
                    et_edit_address_zipcode.text.toString().toInt(),
                    et_edit_address_locality.text.toString(),
                    et_edit_address_mobile.text.toString().toLong(),
                    et_edit_address_Name.text.toString()
                )

                lifecycleScope.launch {
                    when (val response =
                        addressViewModel.updateAddress(
                            Constants.UPDATE,
                            currentUser,
                            args.index,
                            address
                        )) {
                        is Resource.Success -> {
                            if (args.index == -1) {
                                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_LONG)
                                    .show()
                                findNavController().popBackStack()
                            } else {
                                Toast.makeText(requireContext(), "Updated", Toast.LENGTH_LONG)
                                    .show()
                                findNavController().popBackStack()
                            }

                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                response.errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().popBackStack()
                        }
                        else -> Unit
                    }
                }
            } else {
                Toast.makeText(requireContext(), "All Fields are Necessary", Toast.LENGTH_LONG)
                    .show()
            }
        }

        btn_edit_address_cancel.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}
package com.example.shopdrop.presentation.user_address

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.example.shopdrop.presentation.user_address.adapter.AddressAdapter
import com.example.shopdrop.presentation.user_address.viewModel.AddressViewModel
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment : Fragment(R.layout.fragment_address), AddressAdapter.EditClickHandler,
    AddressAdapter.RemoveClickHandler, AddressAdapter.SelectedAddress {

    private lateinit var addressAdapter: AddressAdapter
    private val addressViewModel: AddressViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private val addressList: MutableList<UserAddressDto> = mutableListOf()
    private var selectedIndex: Int = 0
    private val args: AddressFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        addressAdapter = AddressAdapter(requireActivity(), this, addressList, this, this)
        address_recycleView.layoutManager = LinearLayoutManager(context)
        address_recycleView.adapter = addressAdapter

        add_address_fab.setOnClickListener {
            val action = AddressFragmentDirections.actionAddressFragmentToEditAddressFragment()
            findNavController().navigate(action)
        }

        val user = userViewModel.getCurrentUser()
        addressViewModel.getAddress(user!!.uid)
        addressViewModel.address.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    address_progress.isVisible = true
                }
                is Resource.Success -> {
                    address_progress.isVisible = false
                    addressList.clear()
                    addressList.addAll(it.data!!)
                    if (it.data.isNotEmpty()) {
                        btn_confirm_address.isVisible = args.addressSelection
                    } else {
                        btn_confirm_address.isVisible = false
                    }
                    empty_address.isVisible = it.data.isEmpty()
                    addressAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    address_progress.isVisible = false
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        })

        btn_confirm_address.setOnClickListener {
            val previousSavedStateHandle =
                findNavController().previousBackStackEntry!!.savedStateHandle
            previousSavedStateHandle.set(Constants.INDEX, selectedIndex.toString())
            findNavController().popBackStack()
        }
    }


    override fun edit(index: Int) {
        val userAddressDto = addressList[index]
        val action =
            AddressFragmentDirections.actionAddressFragmentToEditAddressFragment(
                userAddressDto,
                index
            )
        findNavController().navigate(action)
    }

    override fun remove(index: Int) {
        val currentUser = userViewModel.getCurrentUser()!!.uid
        lifecycleScope.launch {
            when (val response =
                addressViewModel.updateAddress(
                    Constants.REMOVE,
                    currentUser,
                    index,
                    UserAddressDto()
                )) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Removed", Toast.LENGTH_LONG).show()
                    addressViewModel.getAddress(currentUser)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_LONG)
                        .show()
                }
                else -> Unit
            }
        }

    }

    override fun selectedAddress(index: Int) {
        selectedIndex = index

    }


}
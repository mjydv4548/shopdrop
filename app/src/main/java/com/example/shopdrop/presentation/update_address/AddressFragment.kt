package com.example.shopdrop.presentation.update_address

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopdrop.R
import com.example.shopdrop.presentation.update_address.adapter.AddressAdapter
import kotlinx.android.synthetic.main.fragment_address.*

class AddressFragment : Fragment(R.layout.fragment_address) {

    private lateinit var addressAdapter: AddressAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressAdapter = AddressAdapter(requireActivity())
        address_recycleView.layoutManager = LinearLayoutManager(context)
        address_recycleView.adapter = addressAdapter
    }

}
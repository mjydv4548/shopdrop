package com.example.shopdrop.presentation.user_order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shopdrop.R
import kotlinx.android.synthetic.main.fragment_order_failed.*

class OrderFailedFragment : Fragment(R.layout.fragment_order_failed) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_back_to_cart.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}
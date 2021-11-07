package com.example.shopdrop.presentation.user_cart.model

data class CartItem(
    val image: String = "",
    val description: String = "",
    val price: Int = 0,
    val selectedSize: String = "",
    var quantity: Int = 0,
    val productId: String

)

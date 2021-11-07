package com.example.shopdrop.domain.model

data class Products(
    val images: String = "",
    val description: String = "",
    val price: Int = 0,
    val brand: String = "",
    val productId: String = "",
    var isWishListed: Boolean = false
)

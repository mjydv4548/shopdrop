package com.example.shopdrop.domain.model

data class Wishlist(
    val images: String = "",
    val price: Int = 0,
    val description: String = "",
    val productId: String = "",
    val productAvailableSize: List<String> = mutableListOf()
)
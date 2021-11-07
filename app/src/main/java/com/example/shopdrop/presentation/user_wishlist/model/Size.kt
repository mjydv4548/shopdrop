package com.example.shopdrop.presentation.user_wishlist.model

data class Size(
    var list: MutableList<String> = mutableListOf(),
    var selectedSize: String = ""
)

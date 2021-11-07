package com.example.shopdrop.domain.model

data class Cart(
    val productId: String = "",
    var quantity: Int = 0,
    val selectedSize: String = ""
)

fun Cart.filterList(productId: String, selectedSize: String): Boolean {
    return if (this.productId == productId && this.selectedSize == selectedSize) {
        return true
    } else
        false
}




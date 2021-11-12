package com.example.shopdrop.data.model

import com.example.shopdrop.domain.model.Products
import com.example.shopdrop.domain.model.Review
import com.example.shopdrop.domain.model.Wishlist
import com.example.shopdrop.presentation.user_cart.model.CartItem

data class ProductDto(
    val productAvailableSize: List<String> = mutableListOf(),
    val productBrand: String = "",
    val productDescription: String = "",
    val productDetails: String = "",
    val productFitAndSizing: String = "",
    val productId: String = "",
    val productImages: String = "",
    val productMaterialAndCare: String = "",
    val productPrice: Int = 0,
    val productReviews: List<Review> = mutableListOf(),
    val noOfImages: Int = 0,
    var isWishListed: Boolean = false
)

fun ProductDto.toProducts(): Products {
    return Products(
        images = productImages,
        description = productDescription,
        price = productPrice,
        brand = productBrand,
        productId = productId,
        isWishListed = isWishListed
    )
}

fun ProductDto.toWishlist(): Wishlist {
    return Wishlist(
        images = productImages,
        price = productPrice,
        description = productDescription,
        productId = productId,
        productAvailableSize = productAvailableSize
    )
}

fun ProductDto.toCartItem(selectedSize: String, quantity: Int): CartItem {

    return CartItem(
        image = productImages,
        description = productDescription,
        price = productPrice,
        selectedSize = selectedSize,
        quantity = quantity,
        productId = productId,
        productBrand = productBrand
    )
}


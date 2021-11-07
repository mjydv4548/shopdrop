package com.example.shopdrop.data.model

import com.example.shopdrop.domain.model.Cart

data class UserDto(
    val uid: String = "",
    val userWishlist: List<String> = mutableListOf(),
    val userCart: MutableList<Cart> = mutableListOf(),
    val userAddress: List<UserAddressDto> = mutableListOf(),
    val userProfile: UserProfileDto = UserProfileDto(),
    val isAdmin: Boolean = false
)

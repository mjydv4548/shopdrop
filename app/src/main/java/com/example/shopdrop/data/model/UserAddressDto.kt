package com.example.shopdrop.data.model

data class UserAddressDto(
    val city: String = "",
    val state: String = "",
    val defaultAddress: Boolean = false,
    val streetAddress: String = "",
    val zipCode: Int = 0,
    val locality: String = "",
    val phone: Long = 0
)
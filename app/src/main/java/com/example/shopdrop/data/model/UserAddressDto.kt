package com.example.shopdrop.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserAddressDto(
    val city: String = "",
    val state: String = "",
    var defaultAddress: Boolean = false,
    val streetAddress: String = "",
    val zipCode: Int = 0,
    val locality: String = "",
    val phone: Long = 0,
    val name: String = ""
) : Parcelable

fun UserAddressDto.filterDefault(): Boolean {
    return defaultAddress
}


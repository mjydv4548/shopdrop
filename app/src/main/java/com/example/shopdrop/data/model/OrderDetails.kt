package com.example.shopdrop.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetails(
    val productBrand: String = "",
    val productImage: String = "",
    val productDescription: String = "",
    val productSizeOrdered: String = ""
) : Parcelable

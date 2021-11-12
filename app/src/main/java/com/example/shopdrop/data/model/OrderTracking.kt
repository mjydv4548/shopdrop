package com.example.shopdrop.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderTracking(
    val orderPlaced: Boolean = false,
    val orderConfirmed: Boolean = false,
    val orderShipped: Boolean = false,
    val orderDelivered: Boolean = false,
    val orderProgress: Boolean = false

) : Parcelable

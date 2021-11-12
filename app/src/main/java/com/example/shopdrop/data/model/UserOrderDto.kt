package com.example.shopdrop.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserOrderDto(
    val orderId: String = "",
    val orderTotalPrice: Int = 0,
    val orderQuantity: Int = 0,
    val onGoing: Boolean = false,
    val completed: Boolean = false,
    val cancelled: Boolean = false,
    val orderTracking: OrderTracking = OrderTracking(),
    val orderDetails: OrderDetails = OrderDetails(),
    val deliveryAddress: UserAddressDto = UserAddressDto()
) : Parcelable
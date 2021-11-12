package com.example.shopdrop.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProfileDto(
    var userEmail: String = "",
    var userName: String = "",
    var userPhone: Long = 0,
    val userProfilePicture: String = ""
) : Parcelable
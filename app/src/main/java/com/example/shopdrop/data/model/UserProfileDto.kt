package com.example.shopdrop.data.model

data class UserProfileDto(
    var userEmail: String = "",
    var userName: String = "",
    var userPhone: Long = 0,
    val userProfilePicture: String = ""
)
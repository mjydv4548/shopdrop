package com.example.shopdrop.presentation.user_auth.model

data class User(
    val uid: String,
    val uName: String,
    val uEmail: String,
    val role: String = "user"
)

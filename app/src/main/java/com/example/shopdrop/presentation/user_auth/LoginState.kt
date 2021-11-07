package com.example.shopdrop.presentation.user_auth

data class LoginState(
    val loginSuccessful: Boolean = false,
    val loginError: String = "An unknown error occurred"
)
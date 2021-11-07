package com.example.shopdrop.presentation.user_auth

data class SignUpState(
    val signUpSuccessful: Boolean = false,
    val signUpError: String = "An unknown error occurred"
)
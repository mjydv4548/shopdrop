package com.example.shopdrop.domain.user_case.auth_user

import javax.inject.Inject

data class AuthUserUseCase @Inject constructor(
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val loginUseCase: LoginUseCase,
    val signUpUseCase: SignUpUseCase
)
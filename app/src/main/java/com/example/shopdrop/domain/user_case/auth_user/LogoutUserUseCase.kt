package com.example.shopdrop.domain.user_case.auth_user

import com.example.shopdrop.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke() = authRepository.logOut()
}
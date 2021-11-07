package com.example.shopdrop.domain.user_case.auth_user

import com.example.shopdrop.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke(): FirebaseUser? =
        authRepository.getCurrentUser()
}
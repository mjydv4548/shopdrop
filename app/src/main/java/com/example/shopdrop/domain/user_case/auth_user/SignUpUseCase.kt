package com.example.shopdrop.domain.user_case.auth_user

import com.example.shopdrop.common.Resource
import com.example.shopdrop.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(name: String,email: String,password: String): Resource<FirebaseUser?>
    = authRepository.signUp(name, email, password)
}
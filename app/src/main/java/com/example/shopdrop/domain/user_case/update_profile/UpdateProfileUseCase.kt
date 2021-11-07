package com.example.shopdrop.domain.user_case.update_profile

import android.net.Uri
import com.example.shopdrop.common.Resource
import com.example.shopdrop.domain.repository.UserRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(
        userId: String,
        name: String?,
        email: String?,
        phone: Long?,
        uri: Uri?
    ): Resource<Boolean> = userRepository.updateProfile(userId, name, email, phone, uri)
}
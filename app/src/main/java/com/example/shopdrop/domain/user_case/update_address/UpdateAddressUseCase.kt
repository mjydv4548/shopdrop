package com.example.shopdrop.domain.user_case.update_address

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserAddressDto
import com.example.shopdrop.domain.repository.UserRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(
        action: String,
        userId: String,
        index: Int,
        address: UserAddressDto
    ): Resource<Boolean> = userRepository.updateAddress(action, userId, index, address)
}
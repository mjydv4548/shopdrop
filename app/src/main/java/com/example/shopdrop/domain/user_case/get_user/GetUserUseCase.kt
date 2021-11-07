package com.example.shopdrop.domain.user_case.get_user

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserDto
import com.example.shopdrop.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(userId: String): Flow<Resource<UserDto>> =
        userRepository.getUserById(userId)
}
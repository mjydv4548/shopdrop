package com.example.shopdrop.domain.user_case.get_users

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserDto
import com.example.shopdrop.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(): Flow<Resource<List<UserDto>>> = userRepository.getUsers()
}
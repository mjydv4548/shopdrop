package com.example.shopdrop.domain.repository

import android.net.Uri
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserAddressDto
import com.example.shopdrop.data.model.UserDto
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUsers(): Flow<Resource<List<UserDto>>>

    suspend fun getUserById(userId: String): Flow<Resource<UserDto>>

    suspend fun updateProfile(
        userId: String,
        name: String?,
        email: String?,
        phone: Long?,
        uri: Uri?
    ): Resource<Boolean>


    suspend fun updateAddress(
        action: String,
        userId: String,
        index: Int,
        address: UserAddressDto
    ): Resource<Boolean>
}
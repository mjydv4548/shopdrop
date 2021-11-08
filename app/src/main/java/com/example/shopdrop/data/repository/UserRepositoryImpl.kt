package com.example.shopdrop.data.repository

import android.net.Uri
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.firestore.FireStoreOperations
import com.example.shopdrop.data.model.UserAddressDto
import com.example.shopdrop.data.model.UserDto
import com.example.shopdrop.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val fireStoreOperations: FireStoreOperations
) :
    UserRepository {

    override suspend fun getUsers(): Flow<Resource<List<UserDto>>> = flow {

        try {
            emit(Resource.Loading())
            val result = fireStore.collection("users").get().await()
            val users = result.toObjects(UserDto::class.java)
            emit(Resource.Success(users))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An Unknown error occurred"))
        }


    }

    override suspend fun getUserById(userId: String): Flow<Resource<UserDto>> = flow {
        try {
            emit(Resource.Loading())
            val result = fireStore.collection("users").document(userId).get().await()
            val user = result.toObject(UserDto::class.java)!!
            emit(Resource.Success(user))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An Unknown error occurred"))
        }
    }


    override suspend fun updateProfile(
        userId: String,
        name: String?,
        email: String?,
        phone: Long?,
        uri: Uri?
    ): Resource<Boolean> {
        return fireStoreOperations.updateProfile(userId, name, email, phone, uri)
    }

    override suspend fun updateAddress(
        action: String,
        userId: String,
        index: Int,
        address: UserAddressDto
    ): Resource<Boolean> {
        return fireStoreOperations.updateAddress(action, userId, index, address)
    }

}
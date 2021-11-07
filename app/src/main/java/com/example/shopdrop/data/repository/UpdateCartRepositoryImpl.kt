package com.example.shopdrop.data.repository

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.firestore.FireStoreOperations
import com.example.shopdrop.domain.repository.UpdateCartRepository
import javax.inject.Inject

class UpdateCartRepositoryImpl @Inject constructor(private val fireStoreOperations: FireStoreOperations) :
    UpdateCartRepository {

    override suspend fun updateCart(
        userId: String,
        productId: String,
        selectedSize: String,
        action: String
    ): Resource<Boolean> {
        return fireStoreOperations.updateCart(userId, productId, selectedSize, action)
    }
}
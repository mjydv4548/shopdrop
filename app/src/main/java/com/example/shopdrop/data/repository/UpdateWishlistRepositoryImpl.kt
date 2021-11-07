package com.example.shopdrop.data.repository

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.firestore.FireStoreOperations
import com.example.shopdrop.domain.repository.UpdateWishlistRepository
import javax.inject.Inject

class UpdateWishlistRepositoryImpl @Inject constructor(private val fireStoreOperations: FireStoreOperations) :
    UpdateWishlistRepository {
    override suspend fun updateWishlist(userId: String, productId: String): Resource<Boolean> {
        return fireStoreOperations.updateWishlist(userId, productId)
    }

}
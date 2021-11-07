package com.example.shopdrop.domain.repository

import com.example.shopdrop.common.Resource

interface UpdateWishlistRepository {
    suspend fun updateWishlist(userId: String, productId: String): Resource<Boolean>
}
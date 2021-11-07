package com.example.shopdrop.domain.repository

import com.example.shopdrop.common.Resource

interface UpdateCartRepository {
    suspend fun updateCart(
        userId: String,
        productId: String,
        selectedSize: String,
        action: String
    ): Resource<Boolean>
}
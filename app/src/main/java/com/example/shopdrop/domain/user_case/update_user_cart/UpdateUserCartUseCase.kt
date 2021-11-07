package com.example.shopdrop.domain.user_case.update_user_cart

import com.example.shopdrop.common.Resource
import com.example.shopdrop.domain.repository.UpdateCartRepository
import javax.inject.Inject

class UpdateUserCartUseCase @Inject constructor(private val updateCartRepository: UpdateCartRepository) {

    suspend operator fun invoke(
        userId: String,
        productId: String,
        selectedSize: String,
        action: String
    ): Resource<Boolean> = updateCartRepository.updateCart(userId, productId, selectedSize, action)
}
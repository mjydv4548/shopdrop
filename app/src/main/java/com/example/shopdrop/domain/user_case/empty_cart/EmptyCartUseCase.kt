package com.example.shopdrop.domain.user_case.empty_cart

import com.example.shopdrop.domain.repository.UpdateCartRepository
import javax.inject.Inject

class EmptyCartUseCase @Inject constructor(private val updateCartRepository: UpdateCartRepository) {

    suspend operator fun invoke(userId: String) = updateCartRepository.emptyCart(userId)
}
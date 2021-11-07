package com.example.shopdrop.domain.user_case.update_user_wishlist

import com.example.shopdrop.common.Resource
import com.example.shopdrop.domain.repository.UpdateWishlistRepository
import javax.inject.Inject

class UpdateUserWishlistUseCase @Inject constructor(private val updateWishlistRepository: UpdateWishlistRepository) {

    suspend operator fun invoke(userId: String, productId: String): Resource<Boolean> =
        updateWishlistRepository.updateWishlist(userId, productId)
}
package com.example.shopdrop.domain.user_case.get_product

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.ProductDto
import com.example.shopdrop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(private val productRepository: ProductRepository) {

    suspend operator fun invoke(productId: String): Flow<Resource<ProductDto>> =
        productRepository.getProductById(productId)
}
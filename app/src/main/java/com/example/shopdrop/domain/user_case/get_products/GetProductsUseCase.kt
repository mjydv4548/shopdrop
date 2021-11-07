package com.example.shopdrop.domain.user_case.get_products

import com.example.shopdrop.common.Resource
import com.example.shopdrop.domain.model.Products
import com.example.shopdrop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val productRepository: ProductRepository) {

    suspend operator fun invoke(): Flow<Resource<List<Products>>> = productRepository.getProducts()
}
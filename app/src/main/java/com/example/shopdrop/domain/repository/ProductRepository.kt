package com.example.shopdrop.domain.repository

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.ProductDto
import com.example.shopdrop.domain.model.Products
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProducts(): Flow<Resource<List<Products>>>

    suspend fun getProductById(productId: String): Flow<Resource<ProductDto>>
}
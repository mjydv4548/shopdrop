package com.example.shopdrop.data.repository

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.ProductDto
import com.example.shopdrop.data.model.toProducts
import com.example.shopdrop.domain.model.Products
import com.example.shopdrop.domain.repository.ProductRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class ProductRepositoryImpl @Inject constructor(private val fireStore: FirebaseFirestore) :
    ProductRepository {


    override suspend fun getProducts(): Flow<Resource<List<Products>>> = flow {
        try {
            emit(Resource.Loading())
            val documents = fireStore.collection("products").get().await()
            val products = documents.toObjects(ProductDto::class.java).map { it.toProducts() }
            emit(Resource.Success(products))

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An Unexpected Error Occurred"))
        }
    }

    override suspend fun getProductById(productId: String): Flow<Resource<ProductDto>> = flow {
        try {
            emit(Resource.Loading())
            val result = fireStore.collection("products").document(productId).get().await()
            val productDto = result.toObject(ProductDto::class.java)!!
            emit(Resource.Success(productDto))

        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An Unexpected Error Occurred"))
        }
    }


}
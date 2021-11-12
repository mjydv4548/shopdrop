package com.example.shopdrop.data.repository

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.firestore.FireStoreOperations
import com.example.shopdrop.data.model.UserOrderDto
import com.example.shopdrop.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(private val fireStoreOperations: FireStoreOperations) :
    OrderRepository {

    override suspend fun addOrders(
        userId: String,
        orderDto: List<UserOrderDto>
    ): Resource<Boolean> {
        return fireStoreOperations.addOrder(userId, orderDto)
    }
}
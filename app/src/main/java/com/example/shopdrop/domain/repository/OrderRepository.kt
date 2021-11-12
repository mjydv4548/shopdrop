package com.example.shopdrop.domain.repository

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserOrderDto

interface OrderRepository {

    suspend fun addOrders(userId: String, orderDto: List<UserOrderDto>): Resource<Boolean>
}
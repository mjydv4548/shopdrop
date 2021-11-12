package com.example.shopdrop.domain.user_case.update_orders

import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserOrderDto
import com.example.shopdrop.domain.repository.OrderRepository
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(private val orderRepository: OrderRepository) {

    suspend operator fun invoke(userId: String, orderDto: List<UserOrderDto>): Resource<Boolean> =
        orderRepository.addOrders(userId, orderDto)
}
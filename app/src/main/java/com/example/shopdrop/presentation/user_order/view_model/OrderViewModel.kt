package com.example.shopdrop.presentation.user_order.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserOrderDto
import com.example.shopdrop.domain.user_case.get_user.GetUserUseCase
import com.example.shopdrop.domain.user_case.update_orders.UpdateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val updateOrderUseCase: UpdateOrderUseCase,
    private val getUserUseCase: GetUserUseCase
) :
    ViewModel() {

    private val _addOrders: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val addOrders: LiveData<Resource<Boolean>> get() = _addOrders

    private val _userOrders: MutableLiveData<Resource<List<UserOrderDto>>> = MutableLiveData()
    val userOrders: LiveData<Resource<List<UserOrderDto>>> get() = _userOrders

    fun addOrders(userId: String, orderDto: List<UserOrderDto>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = updateOrderUseCase(userId, orderDto)) {
                is Resource.Loading -> {
                    _addOrders.postValue(Resource.Loading())
                }
                is Resource.Success -> {
                    _addOrders.postValue(Resource.Success(true))
                }
                is Resource.Error -> {
                    _addOrders.postValue(Resource.Error(result.errorMessage.toString()))
                }
            }

        }
    }

    fun getUserOrders(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase(userId).collect { userDto ->
                when (userDto) {
                    is Resource.Loading -> {
                        _userOrders.postValue(Resource.Loading())
                    }
                    is Resource.Success -> {
                        val userOrder = userDto.data!!.userOrders
                        _userOrders.postValue(Resource.Success(userOrder))
                    }
                    is Resource.Error -> {
                        _userOrders.postValue(Resource.Error(userDto.errorMessage.toString()))
                    }
                }
            }
        }
    }

}
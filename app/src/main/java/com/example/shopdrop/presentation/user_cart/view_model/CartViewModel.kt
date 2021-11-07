package com.example.shopdrop.presentation.user_cart.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.toCartItem
import com.example.shopdrop.domain.user_case.get_product.GetProductUseCase
import com.example.shopdrop.domain.user_case.get_user.GetUserUseCase
import com.example.shopdrop.domain.user_case.update_user_cart.UpdateUserCartUseCase
import com.example.shopdrop.presentation.user_cart.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserCartUseCase: UpdateUserCartUseCase
) : ViewModel() {

    private val _cart: MutableLiveData<Resource<List<CartItem>>> = MutableLiveData()
    val cart: LiveData<Resource<List<CartItem>>>
        get() = _cart

    suspend fun updateCart(
        userId: String,
        productId: String,
        selectedSize: String,
        action: String
    ): Resource<Boolean> {
        var result: Resource<Boolean> = Resource.Error("An unknown error occurred")
        viewModelScope.launch(Dispatchers.IO) {
            result = updateUserCartUseCase(userId, productId, selectedSize, action)
        }.join()
        return result
    }

    fun getCart(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {

            getUserUseCase(userId).collect { user ->
                when (user) {
                    is Resource.Loading -> {
                        _cart.postValue(Resource.Loading())
                    }
                    is Resource.Success -> {
                        val userCart = user.data!!.userCart
                        val finalList = mutableListOf<CartItem>()
                        for (cart in userCart) {
                            getProductUseCase(cart.productId).collect { product ->
                                when (product) {
                                    is Resource.Loading -> {
                                        _cart.postValue(Resource.Loading())
                                    }
                                    is Resource.Success -> {
                                        val item = product.data!!.toCartItem(
                                            cart.selectedSize,
                                            cart.quantity
                                        )
                                        finalList.add(item)
                                    }
                                    is Resource.Error -> {
                                        _cart.postValue(Resource.Error(product.errorMessage.toString()))
                                    }
                                }
                            }
                        }
                        _cart.postValue(Resource.Success(finalList))
                    }
                    is Resource.Error -> {
                        _cart.postValue(Resource.Error(user.errorMessage.toString()))
                    }
                }
            }
        }
    }
}
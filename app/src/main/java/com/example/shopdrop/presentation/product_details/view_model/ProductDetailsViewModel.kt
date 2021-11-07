package com.example.shopdrop.presentation.product_details.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.ProductDto
import com.example.shopdrop.domain.user_case.get_product.GetProductUseCase
import com.example.shopdrop.domain.user_case.get_user.GetUserUseCase
import com.example.shopdrop.domain.user_case.update_user_cart.UpdateUserCartUseCase
import com.example.shopdrop.domain.user_case.update_user_wishlist.UpdateUserWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserWishlistUseCase: UpdateUserWishlistUseCase,
    private val updateUserCartUseCase: UpdateUserCartUseCase,
    ) :
    ViewModel() {

    private val _product: MutableLiveData<Resource<ProductDto>> = MutableLiveData()
    val product: LiveData<Resource<ProductDto>>
        get() = _product

    suspend fun updateWishlist(userId: String, productId: String): Resource<Boolean> {
        var result: Resource<Boolean> = Resource.Error("An unknown error occurred")
        viewModelScope.launch(Dispatchers.IO) {
            result = updateUserWishlistUseCase(userId, productId)
        }.join()
        return result
    }


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


    fun getProduct(productId: String, userId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            getProductUseCase(productId).collect {
                when (it) {
                    is Resource.Loading -> {
                        _product.postValue(Resource.Loading())
                    }
                    is Resource.Success -> {
                        if (userId != null) {
                            viewModelScope.launch(Dispatchers.IO) {
                                getUserUseCase(userId).collect { user ->
                                    when (user) {
                                        is Resource.Loading -> {
                                            _product.postValue(Resource.Loading())
                                        }
                                        is Resource.Success -> {
                                            val wishList = user.data?.userWishlist
                                            if (wishList != null) {
                                                if (wishList.contains(it.data!!.productId)) {
                                                    it.data.isWishListed = true
                                                }
                                            }
                                        }
                                        is Resource.Error -> {
                                            _product.postValue(Resource.Error(user.errorMessage.toString()))
                                        }
                                    }
                                }
                            }.join()
                        }
                        _product.postValue(Resource.Success(it.data!!))
                    }
                    is Resource.Error -> {
                        _product.postValue(
                            Resource.Error(
                                it.errorMessage.toString()
                            )
                        )
                    }
                }
            }
        }
    }


}
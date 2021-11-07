package com.example.shopdrop.presentation.product_list.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopdrop.common.Resource
import com.example.shopdrop.domain.model.Products
import com.example.shopdrop.domain.user_case.get_products.GetProductsUseCase
import com.example.shopdrop.domain.user_case.get_user.GetUserUseCase
import com.example.shopdrop.domain.user_case.update_user_wishlist.UpdateUserWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserWishlistUseCase: UpdateUserWishlistUseCase
) :
    ViewModel() {

    private val _productList: MutableLiveData<Resource<List<Products>>> = MutableLiveData()

    val productList: LiveData<Resource<List<Products>>>
        get() = _productList


    suspend fun updateWishlist(userId: String, productId: String): Resource<Boolean> {
        var result: Resource<Boolean> = Resource.Error("An unknown error occurred")
        viewModelScope.launch(Dispatchers.IO) {
            result = updateUserWishlistUseCase(userId, productId)
        }.join()
        return result
    }


    fun getProducts(userId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            getProductsUseCase().collect { product ->
                when (product) {
                    is Resource.Loading -> {
                        _productList.postValue(Resource.Loading())
                    }
                    is Resource.Success -> {
                        if (userId != null) {
                            viewModelScope.launch(Dispatchers.IO) {
                                getUserUseCase(userId).collect { user ->
                                    when (user) {
                                        is Resource.Loading -> {
                                            _productList.postValue(Resource.Loading())
                                        }
                                        is Resource.Success -> {
                                            val list = user.data?.userWishlist
                                            if (list != null) {
                                                for (item in product.data!!) {
                                                    if (list.contains(item.productId)) {
                                                        item.isWishListed = true
                                                    }
                                                }
                                            }
                                        }
                                        is Resource.Error -> {
                                            _productList.postValue(Resource.Error(user.errorMessage.toString()))
                                        }
                                    }
                                }
                            }.join()
                        }
                        _productList.postValue(Resource.Success(product.data!!))
                    }
                    is Resource.Error -> {
                        _productList.postValue(Resource.Error(product.errorMessage!!))
                    }
                }
            }
        }
    }


}
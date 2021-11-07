package com.example.shopdrop.presentation.user_wishlist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.toWishlist
import com.example.shopdrop.domain.model.Wishlist
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
class UserWishlistViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val updateUserWishlistUseCase: UpdateUserWishlistUseCase,
    private val updateUserCartUseCase: UpdateUserCartUseCase
) :
    ViewModel() {

    private val _wishlist: MutableLiveData<Resource<List<Wishlist>>> = MutableLiveData()
    val wishlist: LiveData<Resource<List<Wishlist>>>
        get() = _wishlist

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

    fun getWishList(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase(userId).collect { user ->
                when (user) {
                    is Resource.Loading -> {
                        _wishlist.postValue(Resource.Loading())
                    }
                    is Resource.Success -> {
                        val wishlist = user.data!!.userWishlist
                        val finalList = mutableListOf<Wishlist>()
                        for (list in wishlist) {
                            getProductUseCase(list).collect { product ->
                                when (product) {
                                    is Resource.Loading -> {
                                        _wishlist.postValue(Resource.Loading())
                                    }
                                    is Resource.Success -> {
                                        val item = product.data?.toWishlist()
                                        finalList.add(item!!)
                                    }
                                    is Resource.Error -> {
                                        _wishlist.postValue(Resource.Error(product.errorMessage.toString()))
                                    }
                                }
                            }
                        }
                        _wishlist.postValue(Resource.Success(finalList))
                    }
                    is Resource.Error -> {
                        _wishlist.postValue(Resource.Error(user.errorMessage.toString()))
                    }
                }
            }
        }
    }
}

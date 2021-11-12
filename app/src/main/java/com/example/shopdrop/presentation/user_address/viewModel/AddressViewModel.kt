package com.example.shopdrop.presentation.update_address.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserAddressDto
import com.example.shopdrop.domain.user_case.get_user.GetUserUseCase
import com.example.shopdrop.domain.user_case.update_address.UpdateAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase
) :
    ViewModel() {

    private val _address: MutableLiveData<Resource<List<UserAddressDto>>> = MutableLiveData()

    val address: LiveData<Resource<List<UserAddressDto>>>
        get() = _address


    fun getAddress(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase(userId).collect {
                when (it) {
                    is Resource.Loading -> {
                        _address.postValue(Resource.Loading())
                    }
                    is Resource.Success -> {
                        val userAddress = it.data!!.userAddress
                        _address.postValue(Resource.Success(userAddress))
                    }
                    is Resource.Error -> {
                        _address.postValue(Resource.Error(it.errorMessage.toString()))
                    }
                }
            }
        }
    }

    suspend fun updateAddress(
        action: String,
        userId: String,
        index: Int,
        address: UserAddressDto
    ): Resource<Boolean> {
        var result: Resource<Boolean> = Resource.Error("An unknown error occurred")
        viewModelScope.launch(Dispatchers.IO) {
            result = updateAddressUseCase(action, userId, index, address)
        }.join()
        return result
    }

}
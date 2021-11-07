package com.example.shopdrop.presentation.user_profile.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserDto
import com.example.shopdrop.domain.user_case.auth_user.LogoutUserUseCase
import com.example.shopdrop.domain.user_case.get_user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
) :
    ViewModel() {

    private val _profile: MutableLiveData<Resource<UserDto>> = MutableLiveData()

    val profile: LiveData<Resource<UserDto>> get() = _profile

    fun logout() {
        logoutUserUseCase()
    }

    fun getCurrentUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase(userId).collect { user ->
                when (user) {
                    is Resource.Loading -> {
                        _profile.postValue(Resource.Loading())
                    }
                    is Resource.Success -> {
                        _profile.postValue(Resource.Success(user.data!!))
                    }
                    is Resource.Error -> {
                        _profile.postValue(Resource.Error(user.errorMessage.toString()))
                    }
                }
            }
        }
    }

}
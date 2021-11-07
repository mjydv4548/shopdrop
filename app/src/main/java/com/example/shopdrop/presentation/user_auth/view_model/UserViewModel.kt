package com.example.shopdrop.presentation.user_auth.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopdrop.common.Resource
import com.example.shopdrop.domain.user_case.auth_user.AuthUserUseCase
import com.example.shopdrop.presentation.user_auth.LoginState
import com.example.shopdrop.presentation.user_auth.SignUpState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val authUserUseCase: AuthUserUseCase) :
    ViewModel() {

    fun getCurrentUser(): FirebaseUser? = authUserUseCase.getCurrentUserUseCase()

    suspend fun login(email: String, password: String): LoginState {
        val response = viewModelScope.async {
            withContext(Dispatchers.IO) {
                when (val result = authUserUseCase.loginUseCase(email, password)) {

                    is Resource.Success -> {
                        LoginState(true)
                    }

                    is Resource.Error -> {
                        LoginState(false, result.errorMessage ?: "An Unknown error occurred")
                    }

                    else -> LoginState()
                }
            }
        }
        return response.await()
    }

    suspend fun signUp(name: String, email: String, password: String): SignUpState {
        val response = viewModelScope.async {
            withContext(Dispatchers.IO) {
                when (val result = authUserUseCase.signUpUseCase(name, email, password)) {

                    is Resource.Success -> {
                        SignUpState(true)
                    }

                    is Resource.Error -> {
                        SignUpState(false, result.errorMessage ?: "An Unknown error occurred")
                    }

                    else -> SignUpState()
                }
            }
        }
        return response.await()
    }
}
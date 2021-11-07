package com.example.shopdrop.common

sealed class Resource<T>(val data: T? = null, val errorMessage: String? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Loading<T>() : Resource<T>()
    class Error<T>(errorMessage: String) : Resource<T>(errorMessage = errorMessage)
}
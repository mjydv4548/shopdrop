package com.example.shopdrop.domain.repository

import com.example.shopdrop.common.Resource
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun signUp(name: String, email: String, password: String): Resource<FirebaseUser?>

    suspend fun login(email: String, password: String): Resource<FirebaseUser?>

    fun getCurrentUser(): FirebaseUser?

    fun logOut()
}
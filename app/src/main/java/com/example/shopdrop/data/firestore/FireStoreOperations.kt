package com.example.shopdrop.data.firestore

import android.net.Uri
import com.example.shopdrop.common.Constants
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserDto
import com.example.shopdrop.domain.model.Cart
import com.example.shopdrop.domain.model.filterList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreOperations @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseStorage: StorageReference,
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun updateWishlist(userId: String, productId: String): Resource<Boolean> {
        try {
            val userDto = fireStore.collection("users").document(userId).get().await()
            val map = mutableMapOf<String, Any>()
            if (userDto.exists()) {
                val userWishlist: MutableList<String> =
                    userDto.toObject(UserDto::class.java)!!.userWishlist as MutableList<String>
                if (userWishlist.contains(productId)) {
                    userWishlist.remove(productId)
                } else {
                    userWishlist.add(productId)
                }

                map["userWishlist"] = userWishlist

                fireStore.collection("users").document(userId).set(map, SetOptions.merge()).await()
            }
            return Resource.Success(true)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
    }

    suspend fun updateCart(
        userId: String,
        productId: String,
        selectedSize: String,
        action: String
    ): Resource<Boolean> {
        try {
            val userDto = fireStore.collection("users").document(userId).get().await()

            val map = mutableMapOf<String, Any>()
            if (userDto.exists()) {

                val userCart = userDto.toObject(UserDto::class.java)!!.userCart
                val productList =
                    userCart.filter { it.filterList(productId, selectedSize) }

                if (productList.isNotEmpty()) {
                    val index = userCart.indexOf(productList[0])
                    when (action) {

                        Constants.INCREASE -> {
                            productList[0].quantity += 1
                            userCart.removeAt(index)
                            userCart.add(index, productList[0])
                        }

                        Constants.DECREASE -> {
                            if (productList[0].quantity == 1) {
                                userCart.removeAt(index)
                            } else {
                                productList[0].quantity -= 1
                                userCart.removeAt(index)
                                userCart.add(index, productList[0])
                            }
                        }

                        Constants.REMOVE -> {
                            userCart.removeAt(index)
                        }

                    }

                } else {
                    userCart.add(Cart(productId, 1, selectedSize))
                }

                map["userCart"] = userCart

                fireStore.collection("users").document(userId).set(map, SetOptions.merge()).await()
            }
            return Resource.Success(true)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
    }


    suspend fun updateProfile(
        userId: String,
        name: String?,
        email: String?,
        phone: Long?,
        uri: Uri?
    ): Resource<Boolean> {
        try {
            val userDto = fireStore.collection("users").document(userId).get().await()
            val map = mutableMapOf<String, Any>()
            if (userDto.exists()) {
                val userProfile = userDto.toObject(UserDto::class.java)!!.userProfile

                if (name != null) {
                    userProfile.userName = name
                }
                if (email != null) {
                    userProfile.userEmail = email
                }
                if (phone != null) {
                    userProfile.userPhone = phone
                }

                map["userProfile"] = userProfile

                fireStore.collection("users").document(userId).set(map, SetOptions.merge()).await()

                if (uri != null) {
                    firebaseStorage.child("/user-images/${firebaseAuth.currentUser!!.uid}/profile_image.jpg")
                        .putFile(uri).await()
                }

            }
            return Resource.Success(true)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
    }

}
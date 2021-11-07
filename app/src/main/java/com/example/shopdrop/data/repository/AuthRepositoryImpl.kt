package com.example.shopdrop.data.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.shopdrop.R
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserDto
import com.example.shopdrop.data.model.UserProfileDto
import com.example.shopdrop.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val reference: StorageReference
) :
    AuthRepository {

    private val _currentUser: MutableLiveData<FirebaseUser?> =
        MutableLiveData(firebaseAuth.currentUser)

    override fun getCurrentUser(): FirebaseUser? {
        return _currentUser.value
    }

    override fun logOut() {
        firebaseAuth.signOut()
        _currentUser.postValue(null)
    }


    override suspend fun login(email: String, password: String): Resource<FirebaseUser?> {
        return try {
            val response = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            _currentUser.postValue(response.user)
            Resource.Success(response.user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser?> {
        return try {
            val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (response.user != null) {
                val profile = uploadProfilePicture(response.user!!.uid)
                if (profile) {
                    createUser(
                        response.user!!.email!!,
                        response.user!!.uid, name
                    )
                }

            }
            _currentUser.postValue(response.user)
            Resource.Success(response.user)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred")
        }

    }

    private suspend fun uploadProfilePicture(userId: String): Boolean {

        val uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.resources.getResourcePackageName(
                R.drawable.profile_image
            )
                    + '/' + context.resources.getResourceTypeName(R.drawable.profile_image) + '/' + context.resources.getResourceEntryName(
                R.drawable.profile_image
            )
        )

        return try {
            reference.child("/user-images/$userId/profile_image.jpg").putFile(uri).await()
            true
        } catch (e: Exception) {
            false
        }


    }

    private suspend fun createUser(
        userEmail: String,
        userId: String,
        name: String
    ): Boolean {

        return try {
            val userProfileImage = "/user-images/$userId"
            val userProfileDto = UserProfileDto(
                userEmail = userEmail,
                userProfilePicture = userProfileImage,
                userName = name
            )
            val userDto = UserDto(userProfile = userProfileDto, uid = userId)
            fireStore.collection("users").document(userId).set(userDto).await()
            true
        } catch (e: Exception) {
            false
        }

    }


}






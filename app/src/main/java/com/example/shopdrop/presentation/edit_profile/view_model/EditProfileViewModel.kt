package com.example.shopdrop.presentation.edit_profile.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.shopdrop.common.Resource
import com.example.shopdrop.domain.user_case.update_profile.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase
) :
    ViewModel() {

    suspend fun updateProfile(
        userId: String,
        name: String?,
        email: String?,
        phone: Long?,
        uri: Uri?
    ): Resource<Boolean> {
        return updateProfileUseCase(userId, name, email, phone, uri)
    }
}
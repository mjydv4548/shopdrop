package com.example.shopdrop.presentation.user_profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants
import com.example.shopdrop.common.Constants.PROFILE_PICTURE
import com.example.shopdrop.common.GlideApp
import com.example.shopdrop.common.Resource
import com.example.shopdrop.data.model.UserProfileDto
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import com.example.shopdrop.presentation.user_profile.view_model.ProfileViewModel
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @Inject
    lateinit var reference: StorageReference
    private val userViewModel: UserViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private var userProfile = UserProfileDto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle

        savedStateHandle.getLiveData<Boolean>(Constants.LOGIN_SUCCESSFUL)
            .observe(currentBackStackEntry, Observer { success ->
                if (!success) {
                    val startDestination = navController.graph.startDestination
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(startDestination, true)
                        .build()
                    navController.navigate(startDestination, null, navOptions)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = userViewModel.getCurrentUser()
        if (user != null) {

            profileViewModel.getCurrentUser(user.uid)
            profileViewModel.profile.observe(viewLifecycleOwner, Observer { userDto ->
                when (userDto) {
                    is Resource.Loading -> {
                        profile_progress.isVisible = true
                    }
                    is Resource.Success -> {
                        profile_progress.isVisible = false

                        userProfile = userDto.data!!.userProfile

                        val profilePicture = userDto.data.userProfile.userProfilePicture
                        val userName = userDto.data.userProfile.userName

                        GlideApp.with(requireActivity())
                            .load(reference.child("$profilePicture/${PROFILE_PICTURE}"))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(profile_image)

                        profile_userName.text = userName
                    }
                    is Resource.Error -> {
                        profile_progress.isVisible = false
                        Toast.makeText(requireContext(), userDto.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })

        } else {
            val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        btn_logout.setOnClickListener {
            profileViewModel.logout()
            findNavController().popBackStack()
        }

        btn_edit_profile.setOnClickListener {
            val userName = userProfile.userName
            val userEmail = userProfile.userEmail
            val userPhone = userProfile.userPhone
            val userProfilePicture = userProfile.userProfilePicture
            val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(
                userName,
                userEmail,
                userPhone,
                userProfilePicture
            )
            findNavController().navigate(action)
        }


        btn_address.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToAddressFragment()
            findNavController().navigate(action)
        }
    }
}
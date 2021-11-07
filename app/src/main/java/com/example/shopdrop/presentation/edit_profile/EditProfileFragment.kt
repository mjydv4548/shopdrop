package com.example.shopdrop.presentation.edit_profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants.PROFILE_PICTURE
import com.example.shopdrop.common.GlideApp
import com.example.shopdrop.common.Resource
import com.example.shopdrop.presentation.edit_profile.view_model.EditProfileViewModel
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    @Inject
    lateinit var reference: StorageReference
    private val args: EditProfileFragmentArgs by navArgs()
    private var curFile: Uri? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val updateProfileViewModel: EditProfileViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { it ->
                        curFile = it
                        edit_profile_image.setImageURI(it)
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlideApp.with(requireActivity())
            .load(reference.child("${args.userProfileImage}/${PROFILE_PICTURE}"))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(edit_profile_image)

        editProfile_etName.setText(args.userName)
        editProfile_etEmail.setText(args.userEmail)
        editProfile_etPhone.setText(args.userPhone.toString())



        btn_choose_image.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                resultLauncher.launch(it)
            }
        }

        btn_update_profile.setOnClickListener {
            lifecycleScope.launch {
                val userId = userViewModel.getCurrentUser()!!.uid
                val edit = updateProfileViewModel.updateProfile(
                    userId, editProfile_etName.text.toString(),
                    editProfile_etEmail.text.toString(),
                    editProfile_etPhone.text.toString().toLong(),
                    curFile
                )
                when (edit) {
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), edit.errorMessage, Toast.LENGTH_LONG)
                            .show()
                        findNavController().popBackStack()
                    }
                    else -> Unit
                }
            }

        }


    }

}

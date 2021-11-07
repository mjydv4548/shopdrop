package com.example.shopdrop.presentation.user_auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants.CLICKED_SUCCESSFUL
import com.example.shopdrop.common.Constants.SIGNUP_SUCCESSFUL
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.launch

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var previousSavedStateHandle: SavedStateHandle


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previousSavedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        previousSavedStateHandle.set(SIGNUP_SUCCESSFUL, false)
        previousSavedStateHandle.set(CLICKED_SUCCESSFUL, false)

        btnSignUp.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                lifecycleScope.launch {
                    val response = userViewModel.signUp(name, email, password)
                    if (response.signUpSuccessful) {
                        Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show()
                        previousSavedStateHandle.set(SIGNUP_SUCCESSFUL, true)
                        previousSavedStateHandle.set(CLICKED_SUCCESSFUL, true)
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(context, response.signUpError, Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(
                    context,
                    "name , email and password cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnLogin.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }


}
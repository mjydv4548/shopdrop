package com.example.shopdrop.presentation.user_auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopdrop.R
import com.example.shopdrop.common.Constants
import com.example.shopdrop.common.Constants.LOGIN_SUCCESSFUL
import com.example.shopdrop.presentation.user_auth.view_model.UserViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch


class LoginFragment : Fragment(R.layout.fragment_login) {
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var previousSavedStateHandle: SavedStateHandle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry!!
        val currentSavedStateHandle = currentBackStackEntry.savedStateHandle
        previousSavedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle

        currentSavedStateHandle.getLiveData<Boolean>(Constants.SIGNUP_SUCCESSFUL)
            .observe(currentBackStackEntry, Observer { success ->
                if (success) {
                    previousSavedStateHandle.set(LOGIN_SUCCESSFUL, true)
                    navController.popBackStack()
                }
            })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previousSavedStateHandle.set(LOGIN_SUCCESSFUL, false)


        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val response = userViewModel.login(email, password)
                    if (response.loginSuccessful) {
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        previousSavedStateHandle.set(LOGIN_SUCCESSFUL, true)
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(context, response.loginError, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "email and password cannot be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        btnSignUp.setOnClickListener {

            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }

}
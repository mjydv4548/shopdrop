package com.example.shopdrop.presentation.product_list

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopdrop.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()

        bottom_navigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productDetailsFragment -> hideBottomNav()
                R.id.editProfileFragment -> hideBottomNav()
                R.id.addressFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }

    }

    private fun hideBottomNav() {
        bottom_navigation.visibility = View.GONE

    }

    private fun showBottomNav() {
        bottom_navigation.visibility = View.VISIBLE

    }

}
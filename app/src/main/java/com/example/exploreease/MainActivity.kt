package com.example.exploreease

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.exploreease.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search_place,
                R.id.navigation_personal_preferences
            )
        )
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            val destId = destination.id
//            if (destId == R.id.navigation_home) {
//                navView.selec
//            } else if (destId == R.id.navigation_search_place) {
//                navView.selectedItemId = R.id.navigation_search_place
//            } else {
//                navView.selectedItemId = R.id.navigation_personal_preferences_item
//            }
//        }



        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)





    }

}
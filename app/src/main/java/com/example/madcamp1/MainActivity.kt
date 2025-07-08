package com.example.madcamp1

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.madcamp1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_tab1 -> {
                    navController.popBackStack(R.id.navigation_tab1, false)
                    navController.navigate(R.id.navigation_tab1)
                    true
                }
                R.id.navigation_tab2 -> {
                    navController.popBackStack(R.id.navigation_tab2, false)
                    navController.navigate(R.id.navigation_tab2)
                    true
                }
                R.id.navigation_tab3 -> {
                    navController.popBackStack(R.id.navigation_tab3, false)
                    navController.navigate(R.id.navigation_tab3)
                    true
                }
                else -> false
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tab1,
                R.id.navigation_tab2,
                R.id.navigation_tab3
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
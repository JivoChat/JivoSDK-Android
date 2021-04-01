package com.jivosite.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jivosite.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        val navView: BottomNavigationView = findViewById(R.id.navMenu)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if (binding.container.isDrawerOpen(GravityCompat.END)) {
            binding.container.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }
}
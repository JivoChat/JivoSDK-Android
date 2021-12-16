package com.jivosite.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jivosite.example.databinding.ActivityMainBinding
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SHOW_PUSH = "ExtraShowPush"
        const val ACTION_OPEN_CHAT = "ActionOpenChat"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("JivoLifecycle: Activity - on create")
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        val navView: BottomNavigationView = findViewById(R.id.navMenu)
        navView.setupWithNavController(navController)

        if (intent.getStringExtra(ACTION_OPEN_CHAT) == EXTRA_SHOW_PUSH) {
            navController.navigate(R.id.action_pageMain_to_jivoChat)
        }

    }

    override fun onResume() {
        Timber.d("JivoLifecycle: Activity - on resume")
        super.onResume()
    }

    override fun onPause() {
        Timber.d("JivoLifecycle: Activity - on pause")
        super.onPause()
    }

    override fun onBackPressed() {
        if (binding.container.isDrawerOpen(GravityCompat.END)) {
            binding.container.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }
}
package team.redrock.stickerset.main.screen.main

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import team.redrock.rain.lib.common.config.ui.BaseBindActivity
import team.redrock.stickerset.main.R
import team.redrock.stickerset.main.databinding.ActivityMainBinding

class MainActivity : BaseBindActivity<ActivityMainBinding>() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.navView.setupWithNavController(navController)
    }
}
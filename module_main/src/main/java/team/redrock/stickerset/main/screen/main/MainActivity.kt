package team.redrock.stickerset.main.screen.main

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import team.redrock.rain.lib.common.ui.BaseBindActivity
import team.redrock.stickerset.main.R
import team.redrock.stickerset.main.databinding.ActivityMainBinding

class MainActivity : BaseBindActivity<ActivityMainBinding>() {

    private val navController by lazy {
        binding.navHostFragment.getFragment<NavHostFragment>().navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.navView.setupWithNavController(navController)
    }
}
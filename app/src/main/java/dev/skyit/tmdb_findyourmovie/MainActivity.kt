package dev.skyit.tmdb_findyourmovie

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.skyit.tmdb_findyourmovie.databinding.ActivityMainBinding
import dev.skyit.tmdb_findyourmovie.ui.utils.Loadable

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), Loadable {

    private val binding: ActivityMainBinding by viewBinding(R.id.container)

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override var isLoading: Boolean
        get() = binding.progressIndicator.isVisible
        set(value) {
            binding.progressIndicator.isVisible = value
        }

    fun setAppBarTitle(title: String) {
        supportActionBar?.setTitle(title)
    }

}
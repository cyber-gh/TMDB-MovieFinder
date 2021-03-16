package dev.skyit.tmdb_findyourmovie

import android.content.Intent
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
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.databinding.ActivityMainBinding
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.repo.toDbFormat
import dev.skyit.tmdb_findyourmovie.ui.movie_details.MovieDetailsFragmentArgs
import dev.skyit.tmdb_findyourmovie.ui.utils.Loadable
import dev.skyit.tmdb_findyourmovie.ui.utils.errAlert

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), Loadable {

    private val binding: ActivityMainBinding by viewBinding(R.id.container)

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_my_movies, R.id.navigation_search, R.id.navigation_profile))
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

//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            binding.toolbar.isVisible = destination.id != R.id.webFragment
//        }

        processIntent(intent)
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        processIntent(intent)

    }

    private fun processIntent(intent: Intent?) {
        val it = intent ?: return

        val idx = it.getIntExtra("movieId", -1)
        if (idx == -1) return
//        else errAlert("The id is ${idx}")
        val movie: MovieMinimal = it.getSerializableExtra("movieDetails") as MovieMinimal


        findNavController(R.id.nav_host_fragment).navigate(R.id.movieDetailsFragment, MovieDetailsFragmentArgs(movie.toDbFormat(), null, movie.id).toBundle())
    }

}
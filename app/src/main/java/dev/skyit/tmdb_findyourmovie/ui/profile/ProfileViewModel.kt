package dev.skyit.tmdb_findyourmovie.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.repo.UserRepo
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
        private val api: IMoviesAPIClient,
        private val userRepo: UserRepo
) : ViewModel() {


    val isAuth: Boolean
        get() = userRepo.isAuthenticated

    fun loadData() {
        viewModelScope.launch {

            val movies = api.getTrendingMovies()
            moviesList.postValue(movies)

            Timber.i("Movies are")
            Timber.i(movies.toString())
        }
    }

    val moviesList: MutableLiveData<List<MovieMinimal>> = MutableLiveData()
}
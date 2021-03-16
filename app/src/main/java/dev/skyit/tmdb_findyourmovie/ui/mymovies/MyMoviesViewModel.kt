package dev.skyit.tmdb_findyourmovie.ui.mymovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.generic.BaseViewModel
import dev.skyit.tmdb_findyourmovie.repo.UserRepo
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyMoviesViewModel @Inject constructor(
        private val api: IMoviesAPIClient,
        private val userRepo: UserRepo
) : BaseViewModel() {
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
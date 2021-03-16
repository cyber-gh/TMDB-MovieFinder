package dev.skyit.tmdb_findyourmovie.ui.mymovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.generic.BaseViewModel
import dev.skyit.tmdb_findyourmovie.repo.MoviesToWatchRepo
import dev.skyit.tmdb_findyourmovie.repo.UserRepo
import dev.skyit.tmdb_findyourmovie.repo.WatchedMoviesRepo
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyMoviesViewModel @Inject constructor(
    private val api: IMoviesAPIClient,
    private val moviesToWatchRepo: MoviesToWatchRepo,
    private val watchedMoviesRepo: WatchedMoviesRepo
) : BaseViewModel() {
    val toWatchMoviesList: MutableLiveData<List<MovieDb>> = MutableLiveData()
    val watchedMoviesList: MutableLiveData<List<MovieDb>> = MutableLiveData()

    fun loadWatchLaterMovies() {
        viewModelScope.launch {
            val movies = moviesToWatchRepo.getAllMovies()
            toWatchMoviesList.postValue(movies)
        }
    }

    fun loadWatchedMovies() {
        viewModelScope.launch {
            val movies = watchedMoviesRepo.getAllMovies()
            watchedMoviesList.postValue(movies)
        }
    }

}
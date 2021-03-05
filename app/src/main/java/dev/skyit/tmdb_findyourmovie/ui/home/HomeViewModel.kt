package dev.skyit.tmdb_findyourmovie.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: IMoviesAPIClient
) : ViewModel() {



    fun loadData() {
        viewModelScope.launch {

            val movies = api.getTrendingMovies()

            Timber.i("Movies are")
            Timber.i(movies.toString())
        }
    }
}
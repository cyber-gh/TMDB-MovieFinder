package dev.skyit.tmdb_findyourmovie.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.MovieListType
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val api: IMoviesAPIClient
) : ViewModel() {


    val errorLive: SingleLiveEvent<String> = SingleLiveEvent()

    val recommendedMoviesLive: MutableLiveData<List<MovieMinimal> > = MutableLiveData()
    val trendingMoviesLive: MutableLiveData<List<MovieMinimal> > = MutableLiveData()

    fun loadData() {
        viewModelScope.launch {
            kotlin.runCatching {
                val movies = api.getMoviesList(MovieListType.TRENDING)
                trendingMoviesLive.postValue(movies)
            }.onFailure {
                errorLive.postValue(it.localizedMessage)
            }

            kotlin.runCatching {
                val movies = api.getMoviesList(MovieListType.POPULAR)
                recommendedMoviesLive.postValue(movies)
            }.onFailure {
                errorLive.postValue(it.localizedMessage)
            }
        }
    }

}
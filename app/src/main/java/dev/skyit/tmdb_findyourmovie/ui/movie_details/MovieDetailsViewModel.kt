package dev.skyit.tmdb_findyourmovie.ui.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.models.moviecredits.MovieCredits
import dev.skyit.tmdb_findyourmovie.api.models.moviedetails.MovieDetails
import dev.skyit.tmdb_findyourmovie.api.models.movievideo.MovieVideo
import dev.skyit.tmdb_findyourmovie.utils.LoadingResource
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieModel(
        val movieDetails: MovieDetails,
        val credits: MovieCredits,
        val videos: List<MovieVideo>
)

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
        private val apiClient: IMoviesAPIClient,
        private val state: SavedStateHandle
) : ViewModel() {

    private val movieId: Int? = state.get<Int>("movie_id")

    val movieDetailsLive: MutableLiveData<LoadingResource<MovieModel>> = MutableLiveData()

    fun loadData() {
        val idx = movieId ?: return

        viewModelScope.launch {
            kotlin.runCatching {
                val details = apiClient.getMovieDetails(movieId)
                val credits = apiClient.getMovieCredits(movieId)
                val videos = apiClient.getMovieVideos(movieId)

                movieDetailsLive.postValue(LoadingResource.Success(MovieModel(details, credits, videos)))
            }.onFailure {
                movieDetailsLive.postValue(LoadingResource.Error(it.localizedMessage))
            }
        }
    }


}
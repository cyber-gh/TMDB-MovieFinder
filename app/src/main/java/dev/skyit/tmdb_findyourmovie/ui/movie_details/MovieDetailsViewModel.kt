package dev.skyit.tmdb_findyourmovie.ui.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.models.moviecredits.MovieCredits
import dev.skyit.tmdb_findyourmovie.api.models.moviedetails.MovieDetails
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.api.models.movievideo.MovieVideo
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.repo.MoviesToWatchRepo
import dev.skyit.tmdb_findyourmovie.repo.RecentlyWatchedRepo
import dev.skyit.tmdb_findyourmovie.repo.WatchedMoviesRepo
import dev.skyit.tmdb_findyourmovie.repo.toDbFormat
import dev.skyit.tmdb_findyourmovie.utils.LoadingResource
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieModel(
        val movieDetails: MovieDetails,
        val credits: MovieCredits,
        val videos: List<MovieVideo>,
        val didWatch: Boolean
)

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
        private val apiClient: IMoviesAPIClient,
        private val moviesToWatchRepo: MoviesToWatchRepo,
        private val watchedMoviesRepo: WatchedMoviesRepo,
        private val recentlyWatchedRepo: RecentlyWatchedRepo,
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

                val didWatch = watchedMoviesRepo.getAllMovies().firstOrNull { it.id == idx } != null

                movieDetailsLive.postValue(LoadingResource.Success(MovieModel(details, credits, videos, didWatch)))
            }.onFailure {
                movieDetailsLive.postValue(LoadingResource.Error(it.localizedMessage))
            }
        }
    }

    fun addToWatchLater(movie: MovieDb) {
        viewModelScope.launch {
            moviesToWatchRepo.addMovie(movie)
        }
    }

    fun markAsWatched(movie: MovieDb) {
        viewModelScope.launch {
            watchedMoviesRepo.addMovie(movie)
        }
    }

    fun removeFromWatched(movie: MovieDb) {
        viewModelScope.launch {
            watchedMoviesRepo.deleteMovie(movie)
        }
    }

    fun addToRecentlyWatched(movie: MovieDb) {
        viewModelScope.launch {
            recentlyWatchedRepo.addMovie(movie)
        }
    }

}
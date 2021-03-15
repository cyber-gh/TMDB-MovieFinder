package dev.skyit.tmdb_findyourmovie.ui.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.models.MovieDetails
import dev.skyit.tmdb_findyourmovie.generic.BaseViewModel
import dev.skyit.tmdb_findyourmovie.utils.LoadingResource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
        private val apiClient: IMoviesAPIClient,
        private val state: SavedStateHandle
) : ViewModel() {

    private val movieId: Int? = state.get<Int>("movie_id")

    val movieDetailsLive: MutableLiveData<LoadingResource<MovieDetails>> = MutableLiveData()

    fun loadData() {
        val idx = movieId ?: return

        viewModelScope.launch {
            kotlin.runCatching {
                apiClient.getMovieDetails(idx)
            }.onSuccess {
                movieDetailsLive.postValue(LoadingResource.Success(it))
            }.onFailure {
                movieDetailsLive.postValue(LoadingResource.Error(it.localizedMessage))
            }
        }
    }


}
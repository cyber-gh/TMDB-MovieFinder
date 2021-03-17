package dev.skyit.tmdb_findyourmovie.ui.profile

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.repo.RecentlyWatchedRepo
import dev.skyit.tmdb_findyourmovie.repo.RemoteStorageRepo
import dev.skyit.tmdb_findyourmovie.repo.UserDetails
import dev.skyit.tmdb_findyourmovie.repo.UserRepo
import dev.skyit.tmdb_findyourmovie.utils.LoadingResource
import dev.skyit.tmdb_findyourmovie.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
        private val userRepo: UserRepo,
        private val recentlyWatchedRepo: RecentlyWatchedRepo
) : ViewModel() {
    val recentlyWatchedList: MutableLiveData<List<MovieDb>> = MutableLiveData()

    val state: MutableLiveData<UserDetails?> = MutableLiveData<UserDetails?>(userRepo.currentUser)

    val errorsLive: SingleLiveEvent<String> = SingleLiveEvent()

    fun loadRecentlyWatched() {
        viewModelScope.launch {
            val movies = recentlyWatchedRepo.getAllMovies().take(30)
            recentlyWatchedList.postValue(movies)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            userRepo.signOut()
            state.postValue(null)
        }
    }

    fun uploadAvatar(bitmap: Bitmap) {
        viewModelScope.launch {
            kotlin.runCatching {
                userRepo.uploadAvatar(bitmap)
            }.onSuccess {
                state.postValue(userRepo.currentUser)
            }.onFailure {
                errorsLive.postValue(it.localizedMessage)
            }
        }
    }

}
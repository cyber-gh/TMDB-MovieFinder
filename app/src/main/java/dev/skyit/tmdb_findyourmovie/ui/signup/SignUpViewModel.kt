package dev.skyit.tmdb_findyourmovie.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.repo.UserDetails
import dev.skyit.tmdb_findyourmovie.repo.UserRepo
import dev.skyit.tmdb_findyourmovie.utils.LoadingResource
import dev.skyit.tmdb_findyourmovie.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
        private val userRepo: UserRepo
) : ViewModel() {

    val state: SingleLiveEvent<LoadingResource<UserDetails>> = SingleLiveEvent()

    fun signUp(username: String, email: String, pass: String) {
        viewModelScope.launch {
            state.postValue(LoadingResource.Loading())
            kotlin.runCatching {
                userRepo.signUp(username, email, pass)
            }.onFailure {
                state.postValue(LoadingResource.Error(it.localizedMessage))
            }.onSuccess {
                state.postValue(LoadingResource.Success(it))
            }
        }
    }
}
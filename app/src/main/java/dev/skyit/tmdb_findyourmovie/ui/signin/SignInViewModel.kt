package dev.skyit.tmdb_findyourmovie.ui.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.generic.BaseViewModel
import dev.skyit.tmdb_findyourmovie.repo.UserDetails
import dev.skyit.tmdb_findyourmovie.repo.UserRepo
import dev.skyit.tmdb_findyourmovie.utils.LoadingResult
import dev.skyit.tmdb_findyourmovie.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepo: UserRepo
) : BaseViewModel() {

    val state: SingleLiveEvent<LoadingResult> = SingleLiveEvent()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            state.postValue(LoadingResult.Loading)
            kotlin.runCatching {
                userRepo.login(email, pass)
            }.onFailure {
                state.postValue(LoadingResult.Failure(it.localizedMessage))
            }.onSuccess {
                state.postValue(LoadingResult.Success(it))
            }
        }
    }
}


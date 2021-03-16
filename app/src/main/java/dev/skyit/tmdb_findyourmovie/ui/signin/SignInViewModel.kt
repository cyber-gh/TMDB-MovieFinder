package dev.skyit.tmdb_findyourmovie.ui.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.generic.BaseViewModel
import dev.skyit.tmdb_findyourmovie.repo.UserDetails
import dev.skyit.tmdb_findyourmovie.repo.UserRepo
import dev.skyit.tmdb_findyourmovie.utils.LoadingResource
import dev.skyit.tmdb_findyourmovie.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepo: UserRepo
) : BaseViewModel() {

    val state: SingleLiveEvent<LoadingResource<UserDetails>> = SingleLiveEvent()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            state.postValue(LoadingResource.Loading())
            kotlin.runCatching {
                userRepo.login(email, pass)
            }.onFailure {
                state.postValue(LoadingResource.Error(it.localizedMessage))
            }.onSuccess {
                state.postValue(LoadingResource.Success(it))
            }
        }
    }

    fun signInWithFirebase(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)

        viewModelScope.launch {
            state.postValue(LoadingResource.Loading())

            kotlin.runCatching {
                userRepo.loginWithCredential(credential)
            }.onFailure {
                state.postValue(LoadingResource.Error(it.localizedMessage))
            }.onSuccess {
                state.postValue(LoadingResource.Success(it))
            }
        }

    }
}


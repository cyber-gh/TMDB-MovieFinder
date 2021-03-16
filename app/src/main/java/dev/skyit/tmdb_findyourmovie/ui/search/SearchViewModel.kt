package dev.skyit.tmdb_findyourmovie.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import dev.skyit.tmdb_findyourmovie.ui.utils.snack
import dev.skyit.tmdb_findyourmovie.utils.SingleLiveEvent

@HiltViewModel
class SearchViewModel@Inject constructor(
    private val api: IMoviesAPIClient
) : ViewModel() {

    val searchResults = MutableLiveData<List<MovieMinimal>>()
    private val searchQFlow = Channel<String>()
    val errorState: SingleLiveEvent<String> = SingleLiveEvent()

    init {
        viewModelScope.launch {
            searchQFlow.consumeAsFlow().debounce(300)
                .filter { it.isNotBlank() }
                .collect {
                    updateResults(it)
                }
        }
    }

    private suspend fun updateResults(query: String) {
        kotlin.runCatching {
            api.getMoviesFiltered(query)
        }.onSuccess {
            searchResults.postValue(it)
        }.onFailure {
            Timber.e(it, "API error")
            errorState.postValue(it.localizedMessage)
        }
    }

    fun newSearch(keyword: String) {
        viewModelScope.launch {
            searchQFlow.send(keyword)
        }
    }
}
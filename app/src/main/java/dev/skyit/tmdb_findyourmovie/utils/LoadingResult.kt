package dev.skyit.tmdb_findyourmovie.utils

sealed class LoadingResult {
    object Loading: LoadingResult()
    data class Failure(val msg: String): LoadingResult()
    data class Success<T>(val data: T): LoadingResult()
}
package dev.skyit.tmdb_findyourmovie.utils

sealed class LoadingResource<T>(
        val data: T? = null,
        val errorMessage: String? = null
) {
    class Idle<T>: LoadingResource<T>()
    class Success<T>(data: T) : LoadingResource<T>(data)
    class Loading<T>(data: T? = null, var refreshing: Boolean = false) : LoadingResource<T>(data)
    class Error<T>( message: String?, val errorCode: Int? = null) : LoadingResource<T>(null, message)
}
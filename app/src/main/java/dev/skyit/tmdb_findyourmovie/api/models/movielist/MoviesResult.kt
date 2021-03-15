package dev.skyit.tmdb_findyourmovie.api.models.movielist


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesResult(
        @SerialName("page")
    val page: Int,
        @SerialName("results")
    val movieMinimals: List<MovieMinimal>,
        @SerialName("total_pages")
    val totalPages: Int,
        @SerialName("total_results")
    val totalResults: Int
)
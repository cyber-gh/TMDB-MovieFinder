package dev.skyit.tmdb_findyourmovie.api.models.movievideo


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideos(
    @SerialName("id")
    val id: Int,
    @SerialName("results")
    val movieVideos: List<MovieVideo>
)
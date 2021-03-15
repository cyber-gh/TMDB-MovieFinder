package dev.skyit.tmdb_findyourmovie.api.models.moviedetails


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpokenLanguage(
    @SerialName("iso_639_1")
    val iso6391: String,
    @SerialName("name")
    val name: String
)
package dev.skyit.tmdb_findyourmovie.api.models.settings


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TMDBApiConf(
    @SerialName("change_keys")
    val changeKeys: List<String>,
    @SerialName("images")
    val images: Images
)
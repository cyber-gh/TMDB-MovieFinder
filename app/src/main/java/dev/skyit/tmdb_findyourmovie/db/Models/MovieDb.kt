package dev.skyit.tmdb_findyourmovie.db.Models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "Movies")
data class MovieDb(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String?,
    val voteAverage: Double,
    val backdropPath: String,
    val posterPath: String,
    val releaseDate: String
): java.io.Serializable
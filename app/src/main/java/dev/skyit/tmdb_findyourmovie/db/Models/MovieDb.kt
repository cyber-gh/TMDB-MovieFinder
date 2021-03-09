package dev.skyit.tmdb_findyourmovie.db.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class MovieDb(
    @PrimaryKey val id: Int,
    val title: String,
    val voteAverage: Double,
    val backdropPath: String,
    val posterPath: String,
    val releaseDate: String
)
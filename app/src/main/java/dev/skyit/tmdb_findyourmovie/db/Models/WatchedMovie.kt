package dev.skyit.tmdb_findyourmovie.db.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchedMovies")
data class WatchedMovie(
    @PrimaryKey
    val id: Int,
    val dateAdded: String? = null
)
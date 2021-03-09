package dev.skyit.tmdb_findyourmovie.db.Models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "moviesToWatch")
data class MovieToWatch(
    @PrimaryKey
    val id: Int,
    val dateAdded: String? = null
)

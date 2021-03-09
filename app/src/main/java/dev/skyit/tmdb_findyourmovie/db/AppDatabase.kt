package dev.skyit.tmdb_findyourmovie.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.db.dao.MoviesDao
import dev.skyit.tmdb_findyourmovie.db.dao.MoviesToWatchDao

@Database(entities = [MovieDb::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun moviesToWatchDao(): MoviesToWatchDao

    companion object {
        const val DB_NAME = "tmdb-db"
    }
}
package dev.skyit.tmdb_findyourmovie.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.db.Models.MovieToWatch
import dev.skyit.tmdb_findyourmovie.db.Models.WatchedMovie
import dev.skyit.tmdb_findyourmovie.db.dao.MoviesDao
import dev.skyit.tmdb_findyourmovie.db.dao.MoviesToWatchDao
import dev.skyit.tmdb_findyourmovie.db.dao.WatchedMoviesDao

@Database(entities = [MovieDb::class, MovieToWatch::class, WatchedMovie::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun moviesToWatchDao(): MoviesToWatchDao
    abstract fun watchedMoviesDao(): WatchedMoviesDao

    companion object {
        const val DB_NAME = "tmdb-db"
    }
}
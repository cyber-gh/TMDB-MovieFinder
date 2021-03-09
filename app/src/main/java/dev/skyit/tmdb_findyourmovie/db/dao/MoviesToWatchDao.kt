package dev.skyit.tmdb_findyourmovie.db.dao

import androidx.room.*
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.db.Models.MovieToWatch

@Dao
interface MoviesToWatchDao {
    @Query("select id, title, voteAverage, backdropPath, posterPath, releaseDate from moviesToWatch join movies on id")
    suspend fun getAll(): List<MovieDb>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: MovieToWatch)

    @Delete
    suspend fun deleteMovie(movie: MovieToWatch)
}
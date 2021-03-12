package dev.skyit.tmdb_findyourmovie.db.dao

import androidx.room.*
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.db.Models.MovieToWatch

@Dao
interface MoviesToWatchDao {
    @Query("select mv.id, title, voteAverage, backdropPath, posterPath, releaseDate from moviesToWatch mt join movies mv on mt.id=mv.id")
    suspend fun getAll(): List<MovieDb>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: MovieToWatch)

    @Delete
    suspend fun deleteMovie(movie: MovieToWatch)
}
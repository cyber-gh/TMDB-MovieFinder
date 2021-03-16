package dev.skyit.tmdb_findyourmovie.db.dao

import androidx.room.*
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.db.Models.MovieToWatch
import dev.skyit.tmdb_findyourmovie.db.Models.WatchedMovie


@Dao
interface WatchedMoviesDao {
    @Query("select mv.id, title, voteAverage, backdropPath, posterPath, releaseDate, overview from watchedMovies mt join movies mv on mt.id=mv.id")
    suspend fun getAll(): List<MovieDb>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: WatchedMovie)

    @Delete
    suspend fun deleteMovie(movie: WatchedMovie)
}
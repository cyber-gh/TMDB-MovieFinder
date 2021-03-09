package dev.skyit.tmdb_findyourmovie.db.dao

import androidx.room.*
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb

@Dao
interface MoviesDao {

    @Query("select * from movies")
    suspend fun getAll(): List<MovieDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieDb: MovieDb)

    @Delete
    suspend fun deleteMovie(movieDb: MovieDb)
}
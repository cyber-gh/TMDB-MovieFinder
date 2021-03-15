package dev.skyit.tmdb_findyourmovie.repo

import dev.skyit.tmdb_findyourmovie.api.models.MovieMinimal
import dev.skyit.tmdb_findyourmovie.db.AppDatabase
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.db.Models.MovieToWatch
import dev.skyit.tmdb_findyourmovie.db.dao.MoviesDao
import dev.skyit.tmdb_findyourmovie.db.dao.MoviesToWatchDao
import javax.inject.Inject



interface MoviesToWatchRepo{
    suspend fun getAllMovies(): List<MovieDb>
    suspend fun addMovie(movie: MovieMinimal)
    suspend fun deleteMovie(movieDb: MovieDb)
}

class MoviesToWatchImpl @Inject constructor(private val db: AppDatabase): MoviesToWatchRepo{
    private val mDao: MoviesDao = db.moviesDao()
    private val mLaterDao: MoviesToWatchDao = db.moviesToWatchDao()
    override suspend fun getAllMovies(): List<MovieDb> {
        return mLaterDao.getAll()
    }

    override suspend fun addMovie(movie: MovieMinimal) {
        mDao.insertMovie(movie.toDbFormat())
        mLaterDao.insertMovie(MovieToWatch(movie.id))
    }

    override suspend fun deleteMovie(movieDb: MovieDb) {
        mLaterDao.deleteMovie(MovieToWatch(movieDb.id))
    }


}

fun MovieMinimal.toDbFormat(): MovieDb {
    return MovieDb(
        id = id,
        title = title,
        overview=overview,
        voteAverage = voteAverage,
        backdropPath, posterPath, releaseDate
    )
}
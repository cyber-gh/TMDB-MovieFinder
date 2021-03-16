package dev.skyit.tmdb_findyourmovie.repo

import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.db.AppDatabase
import dev.skyit.tmdb_findyourmovie.db.Models.MovieDb
import dev.skyit.tmdb_findyourmovie.db.Models.MovieToWatch
import dev.skyit.tmdb_findyourmovie.db.Models.WatchedMovie
import dev.skyit.tmdb_findyourmovie.db.dao.MoviesDao
import dev.skyit.tmdb_findyourmovie.db.dao.MoviesToWatchDao
import dev.skyit.tmdb_findyourmovie.db.dao.WatchedMoviesDao
import javax.inject.Inject



interface MoviesToWatchRepo{
    suspend fun getAllMovies(): List<MovieDb>
    suspend fun addMovie(movie: MovieDb)
    suspend fun deleteMovie(movieDb: MovieDb)
}

class MoviesToWatchImpl @Inject constructor(private val db: AppDatabase): MoviesToWatchRepo{
    private val mDao: MoviesDao = db.moviesDao()
    private val mLaterDao: MoviesToWatchDao = db.moviesToWatchDao()
    override suspend fun getAllMovies(): List<MovieDb> {
        return mLaterDao.getAll()
    }

    override suspend fun addMovie(movie: MovieDb) {
        mDao.insertMovie(movie)
        mLaterDao.insertMovie(MovieToWatch(movie.id))
    }

    override suspend fun deleteMovie(movieDb: MovieDb) {
        mLaterDao.deleteMovie(MovieToWatch(movieDb.id))
    }
}


interface WatchedMoviesRepo{
    suspend fun getAllMovies(): List<MovieDb>
    suspend fun addMovie(movie: MovieDb)
    suspend fun deleteMovie(movieDb: MovieDb)
}

class WatchedMoviesRepoImpl @Inject constructor(private val db: AppDatabase): WatchedMoviesRepo{
    private val mDao: MoviesDao = db.moviesDao()
    private val mWatchedRepo: WatchedMoviesDao = db.watchedMoviesDao()
    private val mLaterDao: MoviesToWatchDao = db.moviesToWatchDao()
    override suspend fun getAllMovies(): List<MovieDb> {
        return mWatchedRepo.getAll()
    }

    override suspend fun addMovie(movie: MovieDb) {
        mDao.insertMovie(movie)
        mWatchedRepo.insertMovie(WatchedMovie(movie.id))
    }

    override suspend fun deleteMovie(movieDb: MovieDb) {
        mWatchedRepo.deleteMovie(WatchedMovie(movieDb.id))
        mLaterDao.deleteMovie(MovieToWatch(movieDb.id))
    }
}

fun MovieMinimal.toDbFormat(): MovieDb {
    return MovieDb(
        id = id,
        title = title,
        overview=overview,
        voteAverage = voteAverage,
        backdropPath ?: "", posterPath ?: "", releaseDate
    )
}

//fun MovieDb.toApiFormat(): MovieMinimal {
//    return MovieMinimal(
//        id = id,
//        title = title,
//        overview = overview,
//        voteAverage = voteAverage,
//        backdropPath = backdropPath,
//        posterPath = posterPath,
//        releaseDate = releaseDate,
//        adult = false,
//
//    )
//}
package dev.skyit.tmdb_findyourmovie.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.skyit.tmdb_findyourmovie.api.models.moviecredits.MovieCredits
import dev.skyit.tmdb_findyourmovie.api.models.moviedetails.MovieDetails
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MoviesResult
import dev.skyit.tmdb_findyourmovie.api.models.movievideo.MovieVideo
import dev.skyit.tmdb_findyourmovie.api.models.movievideo.MovieVideos
import dev.skyit.tmdb_findyourmovie.api.models.settings.TMDBApiConf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject


enum class MovieListType(val value: String) {
    LATEST("latest"),
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    NOW_PLAYING("now_playing"),
    TRENDING("trending")
}

interface IMoviesAPIClient {


    interface MoviesAPIService {

        @GET("trending/{media_type}/{interval}")
        suspend fun getTrending(
            @Path("media_type") mediaType: String = "movie",
            @Path("interval") interval: String = "week"
        ): MoviesResult

        @GET("movie/{list_type}")
        suspend fun getMoviesList(
            @Path("list_type") listType: String
        ): MoviesResult


        @GET("configuration")
        suspend fun getConf(): TMDBApiConf


        @GET("movie/{movie_id}")
        suspend fun getMovieDetails(@Path("movie_id") movieId: Int): MovieDetails

        @GET("movie/{movie_id}/credits")
        suspend fun getMovieCredits(
                @Path("movie_id") movieId: Int
        ): MovieCredits

        @GET("movie/{movie_id}/videos")
        suspend fun getVideos(
            @Path("movie_id") movieId: Int
        ): MovieVideos

        @GET("search/movie")
        suspend fun getMoviesFiltered(
            @Query("query") query: String
        ): MoviesResult
    }

    suspend fun getTrendingMovies(): List<MovieMinimal>

    suspend fun getMoviesList(type: MovieListType): List<MovieMinimal>

    suspend fun getMovieDetails(movieId: Int): MovieDetails
    suspend fun getMovieCredits(movieId: Int): MovieCredits
    suspend fun getMovieVideos(movieId: Int): List<MovieVideo>

    suspend fun getMoviesFiltered(filter: String): List<MovieMinimal>
}

class MoviesApiClient @Inject constructor(): IMoviesAPIClient {
    private val apiRoot = "https://api.themoviedb.org/3/"
    private val contentType = "application/json".toMediaType()

    private val apiKey = "9298ce2adec59153432766a85f544355"

    private val httpClient: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original: Request = chain.request()
                    val originalHttpUrl: HttpUrl = original.url

                    val url = originalHttpUrl.newBuilder()
                            .addQueryParameter("api_key", apiKey)
                            .build()

                    val requestBuilder: Request.Builder = original.newBuilder()
                            .url(url)

                    val request: Request = requestBuilder.build()
                    chain.proceed(request)
                }.addInterceptor(interceptor)
                .build()
    }

    @ExperimentalSerializationApi
    private val service: IMoviesAPIClient.MoviesAPIService by lazy {
        Retrofit.Builder()
                .baseUrl(apiRoot)
                .client(httpClient)
                .addConverterFactory(Json{
                    ignoreUnknownKeys = true
                }.asConverterFactory(contentType))
                .build()
                .create(IMoviesAPIClient.MoviesAPIService::class.java)
    }

    @Volatile
    private var config: TMDBApiConf? = null

    private suspend fun getOrSetConfig(): TMDBApiConf {
        if (config == null) {
            config = service.getConf()
        }
        return config!!
    }

    private suspend fun String.getFullPath(): String {
        val conf = getOrSetConfig()
        return "${conf.images.secureBaseUrl}original${this}"
    }

    override suspend fun getTrendingMovies(): List<MovieMinimal> {
        return service.getTrending().movieMinimals.map {
            it.apply {
                it.backdropPath = it.backdropPath?.getFullPath()
                it.posterPath = it.posterPath?.getFullPath()
            }
        }
    }

    override suspend fun getMoviesList(type: MovieListType): List<MovieMinimal> {
        return if (type == MovieListType.TRENDING) getTrendingMovies()
        else service.getMoviesList(type.value).movieMinimals.map {
            it.apply {
                it.backdropPath = it.backdropPath?.getFullPath()
                it.posterPath = it.posterPath?.getFullPath()
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return service.getMovieDetails(movieId).apply {
            posterPath = posterPath?.getFullPath()
            backdropPath = backdropPath?.getFullPath()
        }
    }

    override suspend fun getMovieCredits(movieId: Int): MovieCredits {
        val credits = service.getMovieCredits(movieId)
        return MovieCredits(
                id = credits.id,
                cast = credits.cast.map {
                    it.apply {
                        it.profilePath = it.profilePath?.getFullPath()
                    }
                },
                crew = credits.crew.map {
                    it.apply {
                        it.profilePath = it.profilePath?.getFullPath()
                    }
                }
        )
    }

    override suspend fun getMovieVideos(movieId: Int): List<MovieVideo> {
        return service.getVideos(movieId).movieVideos
    }

    override suspend fun getMoviesFiltered(filter: String): List<MovieMinimal> {
        return service.getMoviesFiltered(filter).movieMinimals.map {
            it.apply {
                it.backdropPath = it.backdropPath?.getFullPath()
                it.posterPath = it.posterPath?.getFullPath()
            }
        }
    }
}
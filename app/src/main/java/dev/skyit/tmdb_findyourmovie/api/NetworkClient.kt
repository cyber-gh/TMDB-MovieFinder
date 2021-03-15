package dev.skyit.tmdb_findyourmovie.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.skyit.tmdb_findyourmovie.api.models.MovieDetails
import dev.skyit.tmdb_findyourmovie.api.models.MovieMinimal
import dev.skyit.tmdb_findyourmovie.api.models.MoviesResult
import dev.skyit.tmdb_findyourmovie.api.models.TMDBApiConf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject


interface IMoviesAPIClient {


    interface MoviesAPIService {

        @GET("trending/{media_type}/{interval}")
        suspend fun getTrending(
            @Path("media_type") mediaType: String = "movie",
            @Path("interval") interval: String = "week"
        ): MoviesResult

        @GET("configuration")
        suspend fun getConf(): TMDBApiConf

        @GET("movie/popular")
        suspend fun getPopular(
//            @Path()
        ): MoviesResult

        @GET("movie/{movie_id}")
        suspend fun getMovieDetails(@Path("movie_id") movieId: Int): MovieDetails
    }

    suspend fun getTrendingMovies(): List<MovieMinimal>
    suspend fun getPopularMovies(): List<MovieMinimal>

    suspend fun getMovieDetails(movieId: Int): MovieDetails
}

class MoviesApiClient @Inject constructor(): IMoviesAPIClient {
    private val apiRoot = "https://api.themoviedb.org/3/"
    private val contentType = "application/json".toMediaType()

    private val apiKey = "9298ce2adec59153432766a85f544355"

    private val httpClient: OkHttpClient by lazy {
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
                }
                .build()
    }

    @ExperimentalSerializationApi
    private val service: IMoviesAPIClient.MoviesAPIService by lazy {
        Retrofit.Builder()
                .baseUrl(apiRoot)
                .client(httpClient)
                .addConverterFactory(Json.asConverterFactory(contentType))
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
                it.backdropPath = it.backdropPath.getFullPath()
                it.posterPath = it.posterPath.getFullPath()
            }
        }
    }

    override suspend fun getPopularMovies(): List<MovieMinimal> {
        return service.getPopular().movieMinimals.map {
            it.apply {
                it.backdropPath = it.backdropPath.getFullPath()
                it.posterPath = it.posterPath.getFullPath()
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return service.getMovieDetails(movieId).apply {
            posterPath = posterPath?.getFullPath()
            backdropPath = backdropPath?.getFullPath()
        }
    }
}
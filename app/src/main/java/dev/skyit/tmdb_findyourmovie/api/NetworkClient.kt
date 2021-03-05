package dev.skyit.tmdb_findyourmovie.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.skyit.tmdb_findyourmovie.api.models.MovieMinimal
import dev.skyit.tmdb_findyourmovie.api.models.MoviesResult
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
    }

    suspend fun getTrendingMovies(): List<MovieMinimal>
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

    override suspend fun getTrendingMovies(): List<MovieMinimal> {
        return service.getTrending().movieMinimals
    }

}
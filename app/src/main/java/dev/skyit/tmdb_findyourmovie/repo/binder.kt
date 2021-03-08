package dev.skyit.tmdb_findyourmovie.repo

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.MoviesApiClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun bindUserRepo(repo: FirebaseUserRepo): UserRepo
}
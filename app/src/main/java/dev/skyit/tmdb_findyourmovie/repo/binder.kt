package dev.skyit.tmdb_findyourmovie.repo

import android.content.Context
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.db.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun bindUserRepo(repo: FirebaseUserRepo): UserRepo

    @Singleton
    @Binds
    abstract fun bindMoviesRepo(toWatchImpl: MoviesToWatchImpl): MoviesToWatchRepo

    @Singleton
    @Binds
    abstract fun bindWatchedMoviesRepo(watchedImpl: WatchedMoviesRepoImpl): WatchedMoviesRepo

    @Singleton
    @Binds
    abstract fun bindRecentlyWatchedRepo(recentlyImpl: RecentlyWatchedRepoImpl): RecentlyWatchedRepo

    @Singleton
    @Binds
    abstract fun bindsFirebaseRemoteStorage(repo: FirebaseRemoteStorageRepo): RemoteStorageRepo
}

@Module
@InstallIn(SingletonComponent::class)
class ExternalAuthModule {


    @Singleton
    @Provides
    fun providesGoogleSignInClient(@ApplicationContext appContext: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(appContext.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val client = GoogleSignIn.getClient(appContext, gso)

        return client
    }
}
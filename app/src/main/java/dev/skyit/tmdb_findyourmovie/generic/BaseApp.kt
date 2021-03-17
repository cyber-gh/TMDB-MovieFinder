package dev.skyit.tmdb_findyourmovie.generic

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.soywiz.klock.DateTime
import com.soywiz.klock.Time
import com.soywiz.klock.days
import dagger.hilt.android.HiltAndroidApp
import dev.skyit.tmdb_findyourmovie.BuildConfig
import dev.skyit.tmdb_findyourmovie.notification.NotificationScheduler
import dev.skyit.tmdb_findyourmovie.notification.NotificationWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class BaseApp : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        NotificationScheduler.scheduleNotification(applicationContext)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory).build()
    }

}
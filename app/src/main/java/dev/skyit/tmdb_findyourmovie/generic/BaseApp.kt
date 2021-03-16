package dev.skyit.tmdb_findyourmovie.generic

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.soywiz.klock.DateTime
import com.soywiz.klock.Time
import com.soywiz.klock.days
import dagger.hilt.android.HiltAndroidApp
import dev.skyit.tmdb_findyourmovie.BuildConfig
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

        scheduleNotification()
    }

    // We want an notification at exactly 8PM
    private fun scheduleNotification() {
        val now = DateTime.now()
        val target = DateTime(now.date, Time(20, 0, 0))

        val delay = if (now > target) {
            val newTarget = target + 1.days
            (newTarget - now).seconds
        } else {
            (target - now).seconds
        }

//        val work = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
//                .setInitialDelay(delay.toLong(), TimeUnit.SECONDS)
//            .build()


        //-----------------
        // USE THIS WORK FOR TESTING
        //-----------------

        val work = PeriodicWorkRequestBuilder<NotificationWorker>(16, TimeUnit.MINUTES)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()


        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("send_notification",ExistingPeriodicWorkPolicy.REPLACE, work)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()

            .setWorkerFactory(workerFactory).build()
    }

}
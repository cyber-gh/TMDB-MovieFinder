package dev.skyit.tmdb_findyourmovie.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.soywiz.klock.DateTime
import com.soywiz.klock.Time
import com.soywiz.klock.days
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    // We want an notification at exactly 8PM every day
    fun scheduleNotification(context: Context) {
        val now = DateTime.nowLocal().local
        val target = DateTime(now.date, Time(20, 0, 0))

        val delay = if (now > target) {
            val newTarget = target + 1.days
            (newTarget - now).seconds
        } else {
            (target - now).seconds
        }

        val work = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay.toLong(), TimeUnit.SECONDS)
            .build()


        //-----------------
        // USE THIS WORK FOR TESTING
        //-----------------

//        val work = PeriodicWorkRequestBuilder<NotificationWorker>(16, TimeUnit.MINUTES)
//                .setInitialDelay(10, TimeUnit.SECONDS)
//                .build()


        WorkManager.getInstance(context).enqueueUniquePeriodicWork("send_notification",
            ExistingPeriodicWorkPolicy.REPLACE, work)
    }
}
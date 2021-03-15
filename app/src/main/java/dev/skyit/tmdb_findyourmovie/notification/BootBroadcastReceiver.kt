package dev.skyit.tmdb_findyourmovie.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.soywiz.klock.DateTime
import com.soywiz.klock.Time
import com.soywiz.klock.days
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class BootBroadcastReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return

        val now = DateTime.now()
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



        WorkManager.getInstance(context).enqueueUniquePeriodicWork("send_notification",
            ExistingPeriodicWorkPolicy.REPLACE, work)

    }
}
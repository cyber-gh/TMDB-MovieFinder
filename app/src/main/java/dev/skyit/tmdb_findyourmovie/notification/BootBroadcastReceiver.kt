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
        NotificationScheduler.scheduleNotification(context)
    }
}
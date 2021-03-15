package dev.skyit.tmdb_findyourmovie.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.skyit.tmdb_findyourmovie.MainActivity
import dev.skyit.tmdb_findyourmovie.R
import dev.skyit.tmdb_findyourmovie.api.IMoviesAPIClient
import dev.skyit.tmdb_findyourmovie.api.MovieListType
import dev.skyit.tmdb_findyourmovie.api.models.movielist.MovieMinimal
import timber.log.Timber

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val apiClient: IMoviesAPIClient
): CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {

        Timber.tag("qwer").w("Started job")
        val movie = apiClient.getMoviesList(MovieListType.TRENDING).firstOrNull() ?: return Result.failure()
        sendNotification(movie)

        return Result.success()
    }

    private fun sendNotification(movie: MovieMinimal) {

        Timber.tag("qwer").w("Trying to send notification")
//         Intent to start when notification is tapped
        val notificationIntent = Intent(appContext, MainActivity::class.java)
        notificationIntent.putExtra("movieId", movie.id)
        notificationIntent.putExtra("movieDetails", movie)
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(appContext)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)
        val pendingIntent: PendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        createNotificationChannel(appContext)
        val builder = NotificationCompat.Builder(appContext, "my_channel")
                .setContentText("Checkout this new movie: ${movie.title}")
                .setContentTitle(movie.title) // Only on api < 26, see createNotificationChannel otherwise
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Default sound, vibration etc
                .setSmallIcon(R.drawable.ic_star_filled)
                // Only on api < 26, see createNotificationChannel otherwise
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(appContext)
        notificationManager.notify(0, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "my_channel",
                "MyApp notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "They will wake you up in the night"
            channel.enableVibration(true)

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}

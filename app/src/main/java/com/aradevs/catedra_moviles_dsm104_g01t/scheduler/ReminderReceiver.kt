package com.aradevs.catedra_moviles_dsm104_g01t.scheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.aradevs.catedra_moviles_dsm104_g01t.R
import com.aradevs.catedra_moviles_dsm104_g01t.main.MainActivity
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.AppDatabase
import com.aradevs.storagemanager.datasources.implementations.DatabaseLocalDataSourceImpl
import com.aradevs.storagemanager.repositories.DatabaseRepository
import com.aradevs.storagemanager.use_cases.GetNotificationsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

const val OPEN_NOTIFICATION_ACTION = "open_notification"
const val SHOW_NOTIFICATION_ACTION = "show_notification"

@AndroidEntryPoint
class ReminderReceiver :
    BroadcastReceiver() {
    private val channelId = "reminders"

    lateinit var notificationsUseCase: GetNotificationsUseCase

    /**
     * Initializes the [notificationsUseCase] variable and calls [showNotification]
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            notificationsUseCase =
                GetNotificationsUseCase(DatabaseRepository(DatabaseLocalDataSourceImpl(
                    AppDatabase.getDatabase(context))))
        }
        when (intent?.action) {
            SHOW_NOTIFICATION_ACTION -> showNotification(intent, context)
            else -> {
                //Do Nothing
            }
        }
    }

    /**
     * Shows notification when an broadcast is received
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun showNotification(intent: Intent?, context: Context?) {
        createNotificationChannel(context!!)

        val id = intent?.getIntExtra("id", 0) ?: 0

        val splashIntent = Intent(context, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.action = OPEN_NOTIFICATION_ACTION
            this.putExtra("notificationId", id)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            splashIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
        GlobalScope.launch(Dispatchers.IO) {
            when (val status = notificationsUseCase()) {
                is Status.Success -> {
                    try {
                        val notification =
                            status.data.first { notification -> notification.id == id }
                        val builder = NotificationCompat.Builder(context, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(context.getString(R.string.reminder))
                            .setStyle(notificationBodyTask(notification))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)

                        with(NotificationManagerCompat.from(context)) {
                            notify(id, builder.build())
                        }
                    } catch (e: Exception) {
                        Timber.d("No notification found")
                    }
                }
                else -> Timber.d("Error getting notifications")
            }
        }
    }

    /**
     * Returns a notification body with the provided [com.aradevs.domain.Notification] object
     */
    private fun notificationBodyTask(
        notification: com.aradevs.domain.Notification,
    ): NotificationCompat.InboxStyle {
        var content = NotificationCompat.InboxStyle()
        content = content.addLine(notification.content)
        return content
    }

    /**
     * Creates a notification channel for android versions above version O
     */
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = channelId
            val descriptionText = "medicines reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
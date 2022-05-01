package com.aradevs.catedra_moviles_dsm104_g01t.scheduler

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aradevs.catedra_moviles_dsm104_g01t.main.MainActivity
import com.aradevs.storagemanager.use_cases.GetMedicinesUseCase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val OPEN_NOTIFICATION_ACTION = "open_notification"
const val SHOW_NOTIFICATION_ACTION = "show_notification"

@AndroidEntryPoint
class ReminderReceiver @Inject constructor(private val getMedicinesUseCase: GetMedicinesUseCase) : BroadcastReceiver() {
    private val CHANNEL_ID = "Recordatorios"
    private val sdfTime = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val sdfStandard = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

    override fun onReceive(context: Context?, intent: Intent?) {

        Timber.i(intent?.action)

        when (intent?.action) {
            SHOW_NOTIFICATION_ACTION -> showNotification(intent, context)
            else -> {
                //Do Nothing
            }
        }
    }

    private fun showNotification(intent: Intent?, context: Context?) {
        //createNotificationChannel(context!!)

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
/*
        val notification = getNotification(id.toString())

        if (notification != null) {

            Log.e("ReminderReceiver", "NOTIFICATION TYPE ${notification.type} - ${notification.id}")

            if (notification.type == "group_activity" && isGroupActivityDelivered(id.toString())) {
                Timber.e("CANCELLED BY ACTIVITY DELIVERED - " + id + " - " + notification.type)
                return
            }

            val calendar = Calendar.getInstance()

            calendar.time = when (notification.type) {
                "group_activity", "task", "GRUPAL", "FORO", "ONLINE", "AULA" -> sdf.parse(
                    notification.endDate
                )!!
                else -> sdf.parse(notification.startDate)!!
            }

            if (Calendar.getInstance().after(calendar) && notification.type != "task") {
                Log.e("ReminderReceiver", "CANCELLED BY AFTER DATE - $id")
                return
            }

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title) //set title
                .setStyle(notificationBodyTask(notification)) // set cont
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                notify(id, builder.build())
            }
        } else {
            Log.e("ReminderReceiver", "CANCELLED BY NOTIFICATION NULL - $id")
        }*/
    }

/*
    private fun notificationBodyTask(
        notification: Notification,
    ): NotificationCompat.InboxStyle {
        var content = NotificationCompat.InboxStyle()
        content = content.addLine(notification.subject)
        return content
    }


    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notifications_channel_name)
            val descriptionText = context.getString(R.string.notifications_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }*/
}
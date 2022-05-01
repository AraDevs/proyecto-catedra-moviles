package com.aradevs.catedra_moviles_dsm104_g01t.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class NotificationScheduler @Inject constructor() {
    fun setupNotification(context: Context, id: Int, date: Date, timeBeforeMin: Int) {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date.time

        calendar.add(Calendar.MINUTE, -1 * timeBeforeMin)

        if (calendar.after(Calendar.getInstance()) || calendar.equals(Calendar.getInstance())) {

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("id", id)
                action = SHOW_NOTIFICATION_ACTION
            }

            val pendingIntent =
                PendingIntent.getBroadcast(context,
                    id,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent)

            Timber.i("Alarm configured at " + calendar.time + ":" + calendar.get(Calendar.SECOND))
        } else {
            Log.i("setupAlarm", "Alarm not configured")
        }
    }

    fun cancelNotification(context: Context, id: Int) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(context,
                    id,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)

            Timber.i("Cancelled - " + id)
        } catch (e: Exception) {
            Timber.e("Error - " + id + " [" + e.message + "]")
        }
    }

}
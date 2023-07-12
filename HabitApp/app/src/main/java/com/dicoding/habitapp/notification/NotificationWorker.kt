package com.dicoding.habitapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.detail.DetailHabitActivity
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIFICATION_CHANNEL_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val habitId = inputData.getInt(HABIT_ID, 0)
    private val habitTitle = inputData.getString(HABIT_TITLE)

    private fun getPendingIntent(habitId: Int): PendingIntent? {
        val intent = Intent(applicationContext, DetailHabitActivity::class.java)
            .putExtra(HABIT_ID, habitId)

        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val prefManager = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify = prefManager.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

        val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
        //TODO 12 : If notification preference on, show notification with pending intent
        if (shouldNotify){
            val notificationBuilder =
                NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(habitTitle)
                    .setContentText(applicationContext.getString(R.string.notify_content))
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentIntent(getPendingIntent(habitId))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelName = applicationContext.getString(R.string.notify_channel_name)
                val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(habitId, notificationBuilder.build())
        }
        return Result.success()
    }
}

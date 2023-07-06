package com.dicoding.todoapp.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.ui.detail.DetailTaskActivity
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID
import com.dicoding.todoapp.utils.TASK_ID

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    private lateinit var taskRepository : TaskRepository
    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } else {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun doWork(): Result {
        //TODO 14 : If notification preference on, get nearest active task from repository and show notification with pending intent
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val notificationPref =  sharedPreferences.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

            if (notificationPref) {
                val task = taskRepository.getNearestActiveTask()

                val pendingIntent = getPendingIntent(task)

                val notificationBuilder =
                    channelName?.let {
                        NotificationCompat.Builder(applicationContext, it)
                            .setContentTitle(task.title)
                            .setContentText(String.format(applicationContext.getString(R.string.notify_content), task.description))
                            .setSmallIcon(R.drawable.ic_notifications)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    }

                val notificationManager = NotificationManagerCompat.from(applicationContext)
                notificationBuilder?.let {
                    notificationManager.notify(task.id, it.build())
                }
            }
        return Result.success()
    }
}

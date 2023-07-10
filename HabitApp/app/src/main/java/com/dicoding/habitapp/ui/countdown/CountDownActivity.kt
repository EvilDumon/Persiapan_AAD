package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIF_UNIQUE_WORK

class CountDownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit

        findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

        val viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

        //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished
        viewModel.setInitialTime(habit.minutesFocus)
        viewModel.currentTimeString.observe(this) {
            findViewById<TextView>(R.id.tv_count_down).text = it
        }
        viewModel.eventCountDownFinish.observe(this, Observer(this::updateButtonState))

        //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.
        val workManager = WorkManager.getInstance(applicationContext)
        viewModel.eventCountDownFinish.observe(this){
            if (it){
                val inputData = Data.Builder()
                    .putInt(HABIT_ID, habit.id)
                    .putString(HABIT_TITLE, habit.title)
                    .build()
                val notificationWorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                    .setInputData(inputData)
                    .build()
                workManager.enqueueUniqueWork(
                    NOTIF_UNIQUE_WORK,
                    ExistingWorkPolicy.REPLACE,
                    notificationWorkRequest
                )
            }
        }
        findViewById<Button>(R.id.btn_start).setOnClickListener {
            viewModel.startTimer()
            updateButtonState(false)
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            workManager.cancelAllWorkByTag(NOTIF_UNIQUE_WORK)
            updateButtonState(true)
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = !isRunning
    }
}
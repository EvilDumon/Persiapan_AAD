package com.dicoding.todoapp.setting

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.*
import com.dicoding.todoapp.R
import com.dicoding.todoapp.notification.NotificationWorker
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Notifications permission granted")
            } else {
                showToast("Notifications will not show without permission")
            }
        }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { preference, newValue ->
                //TODO 13 : Schedule and cancel daily reminder using WorkManager with data channelName
                val channelName = getString(R.string.notify_channel_name)
                val data = Data.Builder().putString("channelName", channelName).build()
                val workManager = WorkManager.getInstance(requireContext())
                val workRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 1, TimeUnit.DAYS)
                    .setInputData(data)
                    .build()
                (preference as SwitchPreference).isChecked = newValue as Boolean
                if (newValue) {
                    //enqueue()
                    workManager.enqueue(workRequest)
                    workManager.getWorkInfoByIdLiveData(workRequest.id)
                } else {
                    //cancelPeriodicTask()
                    workManager.cancelWorkById(workRequest.id)
                }
                true
            }
        }
    }
}
package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NOTIFICATION_CHANNEL_NAME
import com.dicoding.courseschedule.util.NightMode
import java.util.concurrent.TimeUnit

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        val darkModePreference = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        darkModePreference?.setOnPreferenceChangeListener { preference, newValue ->
            val mode = when (newValue as String) {
                resources.getStringArray(R.array.dark_mode_value)[0] -> NightMode.AUTO.value
                resources.getStringArray(R.array.dark_mode_value)[1] -> NightMode.ON.value
                else -> NightMode.OFF.value
            }
            updateTheme(mode)
        }
        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        prefNotification?.setOnPreferenceChangeListener { preference, newValue ->
            val workManager = WorkManager.getInstance(requireContext())
            if (newValue as Boolean) {
                // Enable the daily reminder
                DailyReminder().setDailyReminder(requireContext())
            } else {
                // Cancel the daily reminder
                DailyReminder().cancelAlarm(requireContext())
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}
package com.dicoding.habitapp.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.*
import com.dicoding.habitapp.R
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.DarkMode
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE
import com.dicoding.habitapp.utils.NOTIF_UNIQUE_WORK

class SettingsActivity : AppCompatActivity() {

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
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            //TODO 11 : Update theme based on value in ListPreference
            val darkModePreference = findPreference<ListPreference>(getString(R.string.pref_key_dark))
            darkModePreference?.setOnPreferenceChangeListener { preference, newValue ->
                val mode = when (newValue as String) {
                    resources.getStringArray(R.array.dark_mode_value)[0] -> DarkMode.FOLLOW_SYSTEM.value
                    resources.getStringArray(R.array.dark_mode_value)[1] -> DarkMode.ON.value
                    else -> DarkMode.OFF.value
                }
                updateTheme(mode)
            }
        }

        private fun updateTheme(mode: Int): Boolean {
            setDefaultNightMode(mode)
            requireActivity().recreate()
            return true
        }
    }
}
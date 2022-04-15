package com.example.campusinteligenteiot.ui.drawer.config

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.campusinteligenteiot.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
package com.vozniak.myshoppinglist.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.vozniak.myshoppinglist.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference,rootKey)
    }
}
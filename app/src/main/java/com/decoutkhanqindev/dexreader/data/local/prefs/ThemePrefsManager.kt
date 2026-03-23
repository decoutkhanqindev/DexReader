package com.decoutkhanqindev.dexreader.data.local.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object ThemePrefsManager {
  private const val DATA_STORE_NAME = "theme_prefs_manager"
  internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
}
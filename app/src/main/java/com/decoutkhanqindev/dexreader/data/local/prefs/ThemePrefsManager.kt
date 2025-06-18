package com.decoutkhanqindev.dexreader.data.local.prefs

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

object ThemePrefsManager {
  private const val DATA_STORE_NAME = "theme_prefs_manager"
  val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)
}
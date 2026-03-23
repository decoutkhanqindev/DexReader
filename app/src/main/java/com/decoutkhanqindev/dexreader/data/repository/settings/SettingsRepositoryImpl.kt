package com.decoutkhanqindev.dexreader.data.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.decoutkhanqindev.dexreader.domain.entity.value.settings.ThemeMode
import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
  private val prefsManager: DataStore<Preferences>,
) : SettingsRepository {
  private val themeModePrefsKey = stringPreferencesKey(THEME_MODE_KEY)

  override fun observeThemeMode(): Flow<ThemeMode> =
    prefsManager.data.map { prefs ->
      prefs[themeModePrefsKey]
        ?.let { name -> ThemeMode.entries.find { it.name == name } }
        ?: ThemeMode.SYSTEM
    }

  override suspend fun saveThemeMode(value: ThemeMode) {
    prefsManager.edit { prefs ->
      prefs[themeModePrefsKey] = value.name
    }
  }

  companion object {
    private const val THEME_MODE_KEY = "theme_mode"
  }
}

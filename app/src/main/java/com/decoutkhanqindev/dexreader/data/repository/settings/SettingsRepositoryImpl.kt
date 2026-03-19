package com.decoutkhanqindev.dexreader.data.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.decoutkhanqindev.dexreader.di.ThemeModeKeyQualifier
import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import com.decoutkhanqindev.dexreader.domain.value.settings.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
  private val themePrefsManager: DataStore<Preferences>,
  @param:ThemeModeKeyQualifier private val themeModeKey: String,
) : SettingsRepository {
  private val themeModePrefsKey = intPreferencesKey(themeModeKey)

  override fun observeThemeMode(): Flow<ThemeMode> =
    themePrefsManager.data.map { prefs ->
      ThemeMode.entries[prefs[themeModePrefsKey] ?: 0]
    }

  override suspend fun saveThemeMode(value: ThemeMode) {
    themePrefsManager.edit { prefs ->
      prefs[themeModePrefsKey] = value.ordinal
    }
  }
}

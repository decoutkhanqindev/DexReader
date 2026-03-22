package com.decoutkhanqindev.dexreader.data.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.decoutkhanqindev.dexreader.di.ThemeModeKeyQualifier
import com.decoutkhanqindev.dexreader.domain.entity.value.settings.ThemeMode
import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
  private val prefsManager: DataStore<Preferences>,
  @param:ThemeModeKeyQualifier private val themeModeKey: String,
) : SettingsRepository {
  private val themeModePrefsKey = intPreferencesKey(themeModeKey)

  override fun observeThemeMode(): Flow<ThemeMode> =
    prefsManager.data.map { prefs ->
      ThemeMode.entries.getOrElse(prefs[themeModePrefsKey] ?: 0) { ThemeMode.SYSTEM }
    }

  override suspend fun saveThemeMode(value: ThemeMode) {
    prefsManager.edit { prefs ->
      prefs[themeModePrefsKey] = value.ordinal
    }
  }
}

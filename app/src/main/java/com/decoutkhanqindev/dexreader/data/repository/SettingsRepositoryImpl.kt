package com.decoutkhanqindev.dexreader.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.decoutkhanqindev.dexreader.di.ThemeTypeKeyQualifier
import com.decoutkhanqindev.dexreader.domain.model.ThemeType
import com.decoutkhanqindev.dexreader.domain.repository.SettingsRepository
import com.decoutkhanqindev.dexreader.utils.runSuspendCatching
import com.decoutkhanqindev.dexreader.utils.toResultFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
  private val themePrefsManager: DataStore<Preferences>,
  @ThemeTypeKeyQualifier
  private val themeTypeKey: String
) : SettingsRepository {
  private val themeTypePrefsKey = intPreferencesKey(themeTypeKey)

  override fun observeThemeType(): Flow<Result<ThemeType>> =
    themePrefsManager.data.map { prefs ->
      ThemeType.entries[prefs[themeTypePrefsKey] ?: 0]
    }.toResultFlow()

  override suspend fun setThemeType(value: ThemeType): Result<Unit> =
    runSuspendCatching {
      themePrefsManager.edit { prefs ->
        prefs[themeTypePrefsKey] = value.ordinal
      }
    }
}
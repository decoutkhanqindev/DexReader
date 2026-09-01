package com.decoutkhanqindev.dexreader.data.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toUnexpectedException
import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.entity.value.settings.ThemeMode
import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
  private val prefsManager: DataStore<Preferences>,
) : SettingsRepository {
  private val themeModePrefsKey by lazy {
    stringPreferencesKey(THEME_MODE_KEY)
  }
  private val isOnboardingCompletedPrefsKey by lazy {
    booleanPreferencesKey(IS_ONBOARDING_COMPLETED_KEY)
  }
  private val contentLanguagePrefsKey by lazy {
    stringPreferencesKey(CONTENT_LANGUAGE_KEY)
  }

  override fun observeThemeMode(): Flow<ThemeMode> =
    prefsManager.data.map { prefs ->
      prefs[themeModePrefsKey]
        ?.let { name -> ThemeMode.entries.find { it.name == name } }
        ?: ThemeMode.DARK
    }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun saveThemeMode(value: ThemeMode) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        prefsManager.edit { prefs ->
          prefs[themeModePrefsKey] = value.name
        }
        Unit
      },
      catch = { it.toUnexpectedException() }
    )

  override fun observeIsOnboardingCompleted(): Flow<Boolean> =
    prefsManager.data.map { prefs -> prefs[isOnboardingCompletedPrefsKey] ?: false }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun saveIsOnboardingCompleted(value: Boolean) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        prefsManager.edit { prefs ->
          prefs[isOnboardingCompletedPrefsKey] = value
        }
        Unit
      },
      catch = { it.toUnexpectedException() }
    )

  override fun observeContentLanguage(): Flow<MangaLanguage> =
    prefsManager.data.map { prefs ->
      prefs[contentLanguagePrefsKey]
        ?.let { name -> MangaLanguage.entries.find { it.name == name } }
        ?: MangaLanguage.ENGLISH
    }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun saveContentLanguage(value: MangaLanguage) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        prefsManager.edit { prefs ->
          prefs[contentLanguagePrefsKey] = value.name
        }
        Unit
      },
      catch = { it.toUnexpectedException() }
    )

  companion object {
    private const val THEME_MODE_KEY = "theme_mode"
    private const val IS_ONBOARDING_COMPLETED_KEY = "is_onboarding_completed"
    private const val CONTENT_LANGUAGE_KEY = "content_language"
  }
}

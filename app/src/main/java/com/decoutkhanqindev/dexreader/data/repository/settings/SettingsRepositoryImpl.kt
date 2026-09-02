package com.decoutkhanqindev.dexreader.data.repository.settings

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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
  private val app: Application,
) : SettingsRepository {
  private val Context.prefs: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
  private val themeModeKey: Preferences.Key<String> = stringPreferencesKey(THEME_MODE_KEY)
  private val isOnboardingCompletedKey: Preferences.Key<Boolean> =
    booleanPreferencesKey(IS_ONBOARDING_COMPLETED_KEY)
  private val contentLanguageKey: Preferences.Key<String> =
    stringPreferencesKey(CONTENT_LANGUAGE_KEY)

  override fun observeThemeMode(): Flow<ThemeMode> =
    app.prefs.data.map { prefs ->
      prefs[themeModeKey]
        ?.let { name -> ThemeMode.entries.find { it.name == name } }
        ?: ThemeMode.DARK
    }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun saveThemeMode(value: ThemeMode) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        app.prefs.edit { prefs ->
          prefs[themeModeKey] = value.name
        }
        Unit
      },
      catch = { it.toUnexpectedException() }
    )

  override fun observeIsOnboardingCompleted(): Flow<Boolean> =
    app.prefs.data.map { prefs -> prefs[isOnboardingCompletedKey] ?: true }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun saveIsOnboardingCompleted(value: Boolean) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        app.prefs.edit { prefs ->
          prefs[isOnboardingCompletedKey] = value
        }
        Unit
      },
      catch = { it.toUnexpectedException() }
    )

  override fun observeContentLanguage(): Flow<MangaLanguage> =
    app.prefs.data.map { prefs ->
      prefs[contentLanguageKey]
        ?.let { name -> MangaLanguage.entries.find { it.name == name } }
        ?: MangaLanguage.ENGLISH
    }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun saveContentLanguage(value: MangaLanguage) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        app.prefs.edit { prefs ->
          prefs[contentLanguageKey] = value.name
        }
        Unit
      },
      catch = { it.toUnexpectedException() }
    )

  companion object {
    private const val DATA_STORE_NAME = "dex_reader_prefs"
    private const val THEME_MODE_KEY = "theme_mode"
    private const val IS_ONBOARDING_COMPLETED_KEY = "is_onboarding_completed"
    private const val CONTENT_LANGUAGE_KEY = "content_language"
  }
}

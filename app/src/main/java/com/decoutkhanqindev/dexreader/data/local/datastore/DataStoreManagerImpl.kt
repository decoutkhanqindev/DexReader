package com.decoutkhanqindev.dexreader.data.local.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.recoverCatching
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.withContextCatching
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DataStoreManagerImpl @Inject constructor(
  private val app: Application,
) : DataStoreManager {
  private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
  private val Context.prefs: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

  private val isDarkKey: Preferences.Key<Boolean> = booleanPreferencesKey(IS_DARK_KEY)
  override val isDark: StateFlow<Boolean?> = isDarkKey.asStateFlow(default = DEFAULT_IS_DARK)

  private val selectedLangCodeKey: Preferences.Key<String> = stringPreferencesKey(SELECTED_LANG_CODE_KEY)
  override val selectedLangCode: StateFlow<String?> = selectedLangCodeKey.asStateFlow(default = DEFAULT_SELECTED_LANG_CODE)

  private val isFirstOpenKey: Preferences.Key<Boolean> = booleanPreferencesKey(IS_FIRST_OPEN_KEY)
  override val isFirstOpen: StateFlow<Boolean?> = isFirstOpenKey.asStateFlow(default = DEFAULT_IS_FIRST_OPEN)

  override fun saveIsDark(value: Boolean) {
    edit { prefs -> prefs[isDarkKey] = value }
  }

  override fun saveSelectedLangCode(value: String) {
    edit { prefs -> prefs[selectedLangCodeKey] = value }
  }

  override fun saveIsFirstOpen(value: Boolean) {
    edit { prefs -> prefs[isFirstOpenKey] = value }
  }

  private fun <T : Any> Preferences.Key<T>.asStateFlow(default: T): StateFlow<T?> =
    app.prefs.data
      .map { prefs -> prefs[this] ?: default }
      .recoverCatching { throwable ->
        Timber.e("DataStore read $name failed, falling back to $default: ${throwable.stackTraceToString()}")
        emit(default)
      }
      .stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = null,
      )

  private fun edit(transform: (MutablePreferences) -> Unit) {
    scope.launch {
      withContextCatching(
        action = { app.prefs.edit(transform) },
        catch = { throwable -> Timber.e("DataStore edit failed: ${throwable.stackTraceToString()}") },
      )
    }
  }

  companion object {
    private const val DATA_STORE_NAME = "dex_reader_prefs"
    private const val IS_DARK_KEY = "is_dark"
    private const val SELECTED_LANG_CODE_KEY = "selected_lang_code"
    private const val IS_FIRST_OPEN_KEY = "is_first_open"
    private const val DEFAULT_IS_DARK = true
    private const val DEFAULT_SELECTED_LANG_CODE = "en"
    private const val DEFAULT_IS_FIRST_OPEN = true
  }
}

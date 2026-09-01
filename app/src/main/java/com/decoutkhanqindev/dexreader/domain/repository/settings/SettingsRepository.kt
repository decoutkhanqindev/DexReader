package com.decoutkhanqindev.dexreader.domain.repository.settings

import com.decoutkhanqindev.dexreader.domain.entity.value.manga.MangaLanguage
import com.decoutkhanqindev.dexreader.domain.entity.value.settings.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
  fun observeThemeMode(): Flow<ThemeMode>
  suspend fun saveThemeMode(value: ThemeMode)
  fun observeContentLanguage(): Flow<MangaLanguage>
  suspend fun saveContentLanguage(value: MangaLanguage)
  fun observeIsOnboardingCompleted(): Flow<Boolean>
  suspend fun saveIsOnboardingCompleted(value: Boolean)
}

package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
  fun observeThemeMode(): Flow<ThemeMode>
  suspend fun saveThemeMode(value: ThemeMode)
}
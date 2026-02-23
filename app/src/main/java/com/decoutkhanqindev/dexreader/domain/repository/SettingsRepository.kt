package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.ThemeType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
  fun observeThemeType(): Flow<ThemeType>
  suspend fun setThemeType(value: ThemeType)
}
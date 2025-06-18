package com.decoutkhanqindev.dexreader.domain.usecase.settings

import com.decoutkhanqindev.dexreader.domain.model.ThemeType
import com.decoutkhanqindev.dexreader.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveThemeTypeUseCase @Inject constructor(
  private val settingRepository: SettingsRepository
) {
  operator fun invoke(): Flow<Result<ThemeType>> =
    settingRepository.observeThemeType()
}
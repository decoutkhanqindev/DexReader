package com.decoutkhanqindev.dexreader.domain.usecase.settings

import com.decoutkhanqindev.dexreader.domain.model.ThemeType
import com.decoutkhanqindev.dexreader.domain.repository.SettingsRepository
import javax.inject.Inject

class SetThemeTypeUseCase @Inject constructor(
  private val settingRepository: SettingsRepository
) {
  suspend operator fun invoke(value: ThemeType): Result<Unit> =
    settingRepository.setThemeType(value)
}
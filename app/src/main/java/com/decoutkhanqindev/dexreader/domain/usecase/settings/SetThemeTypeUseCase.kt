package com.decoutkhanqindev.dexreader.domain.usecase.settings

import com.decoutkhanqindev.dexreader.domain.model.ThemeType
import com.decoutkhanqindev.dexreader.domain.repository.SettingsRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class SetThemeTypeUseCase @Inject constructor(
  private val repository: SettingsRepository,
) {
  suspend operator fun invoke(value: ThemeType): Result<Unit> =
    runSuspendResultCatching {
      repository.setThemeType(value)
    }
}
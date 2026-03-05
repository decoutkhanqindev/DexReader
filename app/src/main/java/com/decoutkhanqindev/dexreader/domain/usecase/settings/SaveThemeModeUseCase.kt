package com.decoutkhanqindev.dexreader.domain.usecase.settings

import com.decoutkhanqindev.dexreader.domain.model.ThemeMode
import com.decoutkhanqindev.dexreader.domain.repository.SettingsRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class SaveThemeModeUseCase @Inject constructor(
  private val repository: SettingsRepository,
) {
  suspend operator fun invoke(value: ThemeMode): Result<Unit> =
    runSuspendResultCatching { repository.saveThemeMode(value) }
}
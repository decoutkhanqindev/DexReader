package com.decoutkhanqindev.dexreader.domain.usecase.settings

import com.decoutkhanqindev.dexreader.domain.entity.value.settings.ThemeMode
import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendResultCatching
import javax.inject.Inject

class SaveThemeModeUseCase @Inject constructor(
  private val repository: SettingsRepository,
) {
  suspend operator fun invoke(value: ThemeMode): Result<Unit> =
    runSuspendResultCatching { repository.saveThemeMode(value) }
}
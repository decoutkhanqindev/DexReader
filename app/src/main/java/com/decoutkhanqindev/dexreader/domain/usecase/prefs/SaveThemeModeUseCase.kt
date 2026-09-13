package com.decoutkhanqindev.dexreader.domain.usecase.prefs

import com.decoutkhanqindev.dexreader.domain.entity.value.settings.ThemeMode
import com.decoutkhanqindev.dexreader.domain.repository.prefs.PrefsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendResultCatching
import javax.inject.Inject

class SaveThemeModeUseCase @Inject constructor(
  private val repository: PrefsRepository,
) {
  suspend operator fun invoke(value: ThemeMode): Result<Unit> =
    runSuspendResultCatching { repository.saveThemeMode(value) }
}
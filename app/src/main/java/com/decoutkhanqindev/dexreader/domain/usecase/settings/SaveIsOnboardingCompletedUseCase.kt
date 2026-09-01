package com.decoutkhanqindev.dexreader.domain.usecase.settings

import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendResultCatching
import javax.inject.Inject

class SaveIsOnboardingCompletedUseCase @Inject constructor(
  private val repository: SettingsRepository,
) {
  suspend operator fun invoke(value: Boolean): Result<Unit> =
    runSuspendResultCatching { repository.saveIsOnboardingCompleted(value) }
}

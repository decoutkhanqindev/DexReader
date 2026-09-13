  package com.decoutkhanqindev.dexreader.domain.usecase.prefs

import com.decoutkhanqindev.dexreader.domain.repository.prefs.PrefsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendResultCatching
import javax.inject.Inject

class SaveIsOnboardingCompletedUseCase @Inject constructor(
  private val repository: PrefsRepository,
) {
  suspend operator fun invoke(value: Boolean): Result<Unit> =
    runSuspendResultCatching { repository.saveIsOnboardingCompleted(value) }
}

package com.decoutkhanqindev.dexreader.domain.usecase.settings

import com.decoutkhanqindev.dexreader.domain.repository.settings.SettingsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsOnboardingCompletedUseCase @Inject constructor(
  private val repository: SettingsRepository,
) {
  operator fun invoke(): Flow<Result<Boolean>> =
    repository.observeIsOnboardingCompleted().toFlowResult()
}

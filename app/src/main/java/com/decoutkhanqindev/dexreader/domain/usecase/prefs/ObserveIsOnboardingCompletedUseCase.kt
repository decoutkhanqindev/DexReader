package com.decoutkhanqindev.dexreader.domain.usecase.prefs

import com.decoutkhanqindev.dexreader.domain.repository.prefs.PrefsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsOnboardingCompletedUseCase @Inject constructor(
  private val repository: PrefsRepository,
) {
  operator fun invoke(): Flow<Result<Boolean>> =
    repository.observeIsOnboardingCompleted().toFlowResult()
}

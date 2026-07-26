package com.decoutkhanqindev.dexreader.domain.usecase.user.statistics

import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.decoutkhanqindev.dexreader.domain.repository.user.StatisticsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendResultCatching
import javax.inject.Inject

class IncrementReadingDurationUseCase @Inject constructor(
  private val repository: StatisticsRepository,
) {
  suspend operator fun invoke(userId: String, durationMillis: Long): Result<Unit> =
    runSuspendResultCatching {
      val date = ReadingStats.getCurrentDate()
      repository.incrementReadingDuration(userId, date, durationMillis)
    }
}

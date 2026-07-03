package com.decoutkhanqindev.dexreader.domain.usecase.user.statistics

import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.decoutkhanqindev.dexreader.domain.repository.user.StatisticsRepository
import javax.inject.Inject

class IncrementReadingDurationUseCase @Inject constructor(
  private val repository: StatisticsRepository,
) {
  suspend operator fun invoke(userId: String, durationMillis: Long): Result<Unit> {
    val date = ReadingStats.getCurrentDate()
    return repository.incrementReadingDuration(userId, date, durationMillis)
  }
}

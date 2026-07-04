package com.decoutkhanqindev.dexreader.domain.usecase.user.statistics

import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.decoutkhanqindev.dexreader.domain.repository.user.StatisticsRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveStatisticsUseCase @Inject constructor(
  private val repository: StatisticsRepository,
) {
  operator fun invoke(userId: String): Flow<Result<List<ReadingStats>>> =
    repository.observeStatistics(userId)
      .map { Result.success(it) }
      .catch { emit(Result.failure(it)) }
}

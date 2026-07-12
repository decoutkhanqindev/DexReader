package com.decoutkhanqindev.dexreader.domain.usecase.user.statistics

import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.decoutkhanqindev.dexreader.domain.repository.user.StatisticsRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.toFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveStatisticsUseCase @Inject constructor(
  private val repository: StatisticsRepository,
) {
  operator fun invoke(userId: String): Flow<Result<List<ReadingStats>>> =
    repository.observeStatistics(userId).toFlowResult()
}

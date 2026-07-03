package com.decoutkhanqindev.dexreader.domain.repository.user

import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
  suspend fun incrementReadingDuration(userId: String, date: String, durationMillis: Long): Result<Unit>
  fun observeStatistics(userId: String): Flow<List<ReadingStats>>
}

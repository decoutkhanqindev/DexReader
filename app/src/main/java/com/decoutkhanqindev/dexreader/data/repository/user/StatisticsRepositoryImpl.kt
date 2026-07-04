package com.decoutkhanqindev.dexreader.data.repository.user

import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreFlowException
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.statistics.FirebaseStatisticsFirestoreSource
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.decoutkhanqindev.dexreader.domain.repository.user.StatisticsRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
  private val firestoreSource: FirebaseStatisticsFirestoreSource,
) : StatisticsRepository {
  override suspend fun incrementReadingDuration(
    userId: String,
    date: String,
    durationMillis: Long,
  ): Result<Unit> = runSuspendResultCatching {
    firestoreSource.incrementReadingDuration(userId, date, durationMillis)
  }

  override fun observeStatistics(userId: String): Flow<List<ReadingStats>> =
    firestoreSource.observeStatistics(userId)
      .map { list ->
        list.map { ReadingStats(date = it.date, durationMillis = it.durationMillis) }
      }
      .catch { e -> throw e.toFirebaseFirestoreFlowException() }
      .flowOn(Dispatchers.IO)
}

package com.decoutkhanqindev.dexreader.data.repository.user

import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreFlowException
import com.decoutkhanqindev.dexreader.data.mapper.ReadingStatsMapper.toReadingStats
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.statistics.FirebaseStatisticsFirestoreSource
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.decoutkhanqindev.dexreader.domain.repository.user.StatisticsRepository
import com.decoutkhanqindev.dexreader.util.CoroutineHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
  private val firestoreSource: FirebaseStatisticsFirestoreSource,
) : StatisticsRepository {
  override suspend fun incrementReadingDuration(
    userId: String,
    readingStats: ReadingStats,
  ) = runSuspendCatching(
    context = Dispatchers.IO,
    block = {
      firestoreSource.incrementReadingDuration(
        userId,
        readingStats.date,
        readingStats.durationMillis
      )
    },
    catch = { it.toFirebaseFirestoreException() }
  )

  override fun observeStatistics(userId: String): Flow<List<ReadingStats>> =
    firestoreSource.observeStatistics(userId)
      .map { list -> list.map { it.toReadingStats() } }
      .catch { e -> e.toFirebaseFirestoreFlowException() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()
}

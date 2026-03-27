package com.decoutkhanqindev.dexreader.data.repository.user

import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreFlowException
import com.decoutkhanqindev.dexreader.data.mapper.ReadingHistoryMapper.toReadingHistory
import com.decoutkhanqindev.dexreader.data.mapper.ReadingHistoryMapper.toReadingHistoryRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.history.FirebaseHistoryFirestoreSource
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.repository.user.HistoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
  private val firestoreSource: FirebaseHistoryFirestoreSource,
) : HistoryRepository {
  override fun observeHistory(
    userId: String,
    limit: Int,
    mangaId: String?,
    lastReadingHistoryId: String?,
  ): Flow<List<ReadingHistory>> =
    firestoreSource
      .observeHistory(
        userId = userId,
        limit = limit.toLong(),
        mangaId = mangaId,
        lastReadingHistoryId = lastReadingHistoryId
      )
      .map { items -> items.map { it.toReadingHistory() } }
      .catch { e -> e.toFirebaseFirestoreFlowException() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun upsertHistory(
    userId: String,
    readingHistory: ReadingHistory,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        firestoreSource.upsertHistory(
          userId = userId,
          readingHistory = readingHistory.toReadingHistoryRequest()
        )
      },
      catch = { it.toFirebaseFirestoreException() }
    )

  override suspend fun removeFromHistory(
    userId: String,
    readingHistoryId: String,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        firestoreSource.removeFromHistory(
          userId = userId,
          readingHistoryId = readingHistoryId
        )
      },
      catch = { it.toFirebaseFirestoreException() }
    )
}

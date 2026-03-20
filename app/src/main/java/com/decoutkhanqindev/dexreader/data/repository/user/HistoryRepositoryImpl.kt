package com.decoutkhanqindev.dexreader.data.repository.user

import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirestoreException
import com.decoutkhanqindev.dexreader.data.mapper.ReadingHistoryMapper.toReadingHistory
import com.decoutkhanqindev.dexreader.data.mapper.ReadingHistoryMapper.toReadingHistoryRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.repository.user.HistoryRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl
@Inject
constructor(
  private val firebaseFirestoreSource: FirebaseFirestoreSource,
) : HistoryRepository {
  override fun observeHistory(
    userId: String,
    limit: Int,
    mangaId: String?,
    lastReadingHistoryId: String?,
  ): Flow<List<ReadingHistory>> =
    firebaseFirestoreSource
      .observeHistory(
        userId = userId,
        limit = limit.toLong(),
        mangaId = mangaId,
        lastReadingHistoryId = lastReadingHistoryId
      )
      .map { readingHistoryResponseList ->
        readingHistoryResponseList.map { it.toReadingHistory() }
      }
      .catch { e ->
        if (e is FirebaseFirestoreException &&
          e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED
        )
          throw BusinessException.Resource.AccessDenied(rootCause = e)
        else throw e
      }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun addAndUpdateToHistory(
    userId: String,
    readingHistory: ReadingHistory,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        firebaseFirestoreSource.upsertHistory(
          userId = userId,
          readingHistory = readingHistory.toReadingHistoryRequest()
        )
      },
      onCatch = { it.toFirestoreException() }
    )

  override suspend fun removeFromHistory(
    userId: String,
    readingHistoryId: String,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        firebaseFirestoreSource.removeFromHistory(
          userId = userId,
          readingHistoryId = readingHistoryId
        )
      },
      onCatch = { it.toFirestoreException() }
    )
}

package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.toDomain
import com.decoutkhanqindev.dexreader.data.mapper.toDto
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.domain.model.ReadingHistory
import com.decoutkhanqindev.dexreader.domain.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
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
      .map { readingHistoryDtoList ->
        readingHistoryDtoList.map { it.toDomain() }
      }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun addAndUpdateToHistory(
    userId: String,
    readingHistory: ReadingHistory,
  ) = withContext(Dispatchers.IO) {
    firebaseFirestoreSource.addAndUpdateToHistory(
      userId = userId,
      readingHistory = readingHistory.toDto()
    )
  }

  override suspend fun removeFromHistory(
    userId: String,
    readingHistoryId: String,
  ) = withContext(Dispatchers.IO) {
    firebaseFirestoreSource.removeFromHistory(
      userId = userId,
      readingHistoryId = readingHistoryId
    )
  }
}

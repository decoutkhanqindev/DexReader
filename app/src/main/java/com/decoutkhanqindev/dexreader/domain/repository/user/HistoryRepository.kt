package com.decoutkhanqindev.dexreader.domain.repository.user

import com.decoutkhanqindev.dexreader.domain.model.user.ReadingHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
  fun observeHistory(
    userId: String,
    limit: Int = 10,
    mangaId: String? = null,
    lastReadingHistoryId: String? = null,
  ): Flow<List<ReadingHistory>>

  suspend fun addAndUpdateToHistory(userId: String, readingHistory: ReadingHistory)
  suspend fun removeFromHistory(userId: String, readingHistoryId: String)
}

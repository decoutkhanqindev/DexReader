package com.decoutkhanqindev.dexreader.data.network.firebase.firestore.history

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.ReadingHistoryRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.ReadingHistoryResponse
import kotlinx.coroutines.flow.Flow

interface FirebaseHistoryFirestoreSource {
  fun observeHistory(
    userId: String,
    limit: Long,
    mangaId: String? = null,
    lastReadingHistoryId: String? = null,
  ): Flow<List<ReadingHistoryResponse>>

  suspend fun upsertHistory(userId: String, readingHistory: ReadingHistoryRequest)
  suspend fun removeFromHistory(userId: String, readingHistoryId: String)
}

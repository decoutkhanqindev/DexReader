package com.decoutkhanqindev.dexreader.data.network.firebase.firestore.statistics

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.ReadingStatsResponse
import kotlinx.coroutines.flow.Flow

interface FirebaseStatisticsFirestoreSource {
  suspend fun incrementReadingDuration(userId: String, date: String, durationMillis: Long)
  fun observeStatistics(userId: String): Flow<List<ReadingStatsResponse>>
}

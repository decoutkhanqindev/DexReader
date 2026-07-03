package com.decoutkhanqindev.dexreader.data.network.firebase.firestore.statistics

import com.decoutkhanqindev.dexreader.data.network.firebase.constant.FirestoreCollections
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.ReadingStatsResponse
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseStatisticsFirestoreSourceImpl @Inject constructor(
  private val firebaseFirestore: FirebaseFirestore,
) : FirebaseStatisticsFirestoreSource {

  override suspend fun incrementReadingDuration(userId: String, date: String, durationMillis: Long) {
    val statsId = ReadingStats.generateId(userId, date)
    val data = mapOf(
      "user_id" to userId,
      "date" to date,
      "duration_millis" to FieldValue.increment(durationMillis)
    )
    Timber.tag("StatisticsDebug").d("Incrementing duration: $durationMillis for user: $userId on date: $date")
    firebaseFirestore.collection(FirestoreCollections.STATISTICS)
      .document(statsId)
      .set(data, SetOptions.merge())
      .await()
  }

  override fun observeStatistics(userId: String): Flow<List<ReadingStatsResponse>> = callbackFlow {
    Timber.tag("StatisticsDebug").d("Observing stats for user: $userId")
    val listenerRegistration = firebaseFirestore.collection(FirestoreCollections.STATISTICS)
      .whereEqualTo("user_id", userId)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          Timber.tag("StatisticsDebug").e(error, "Firestore error observing stats")
          close(error)
          return@addSnapshotListener
        }

        val statsList = snapshot?.documents?.mapNotNull { document ->
          document.toObject(ReadingStatsResponse::class.java).also {
            Timber.tag("StatisticsDebug").d("Received doc: ${document.data}")
          }
        } ?: emptyList()

        Timber.tag("StatisticsDebug").d("Total stats received: ${statsList.size}")
        trySend(statsList)
      }

    awaitClose { listenerRegistration.remove() }
  }
}

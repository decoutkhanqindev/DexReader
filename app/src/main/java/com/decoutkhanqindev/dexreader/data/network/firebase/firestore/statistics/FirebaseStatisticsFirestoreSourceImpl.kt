package com.decoutkhanqindev.dexreader.data.network.firebase.firestore.statistics

import com.decoutkhanqindev.dexreader.data.network.firebase.constant.FirestoreCollections
import com.decoutkhanqindev.dexreader.data.network.firebase.constant.FirestoreFields
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.ReadingStatsResponse
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStatisticsFirestoreSourceImpl @Inject constructor(
  private val firebaseFirestore: FirebaseFirestore,
) : FirebaseStatisticsFirestoreSource {

  override suspend fun incrementReadingDuration(
    userId: String,
    date: String,
    durationMillis: Long,
  ) {
    val documentRef = firebaseFirestore.collection(FirestoreCollections.STATISTICS)
      .document(ReadingStats.generateId(userId, date))

    documentRef.set(
      mapOf(
        FirestoreFields.USER_ID to userId,
        FirestoreFields.DATE to date,
        FirestoreFields.DURATION_MILLIS to FieldValue.increment(durationMillis),
      ),
      SetOptions.merge(),
    ).await()
  }

  override fun observeStatistics(userId: String): Flow<List<ReadingStatsResponse>> = callbackFlow {
    val listenerRegistration = firebaseFirestore.collection(FirestoreCollections.STATISTICS)
      .whereEqualTo(FirestoreFields.USER_ID, userId)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          close(error)
          return@addSnapshotListener
        }

        val statsList = snapshot?.documents?.mapNotNull { document ->
          document.toObject(ReadingStatsResponse::class.java)
        } ?: emptyList()

        trySend(statsList)
      }

    awaitClose { listenerRegistration.remove() }
  }
}

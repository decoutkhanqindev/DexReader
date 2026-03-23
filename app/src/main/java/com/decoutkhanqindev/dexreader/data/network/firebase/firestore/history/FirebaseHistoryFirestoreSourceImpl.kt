package com.decoutkhanqindev.dexreader.data.network.firebase.firestore.history

import com.decoutkhanqindev.dexreader.data.network.firebase.constant.FirestoreCollections
import com.decoutkhanqindev.dexreader.data.network.firebase.constant.FirestoreFields
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.ReadingHistoryRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.ReadingHistoryResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseHistoryFirestoreSourceImpl @Inject constructor(
  firebaseFirestore: FirebaseFirestore,
) : FirebaseHistoryFirestoreSource {
  private val usersCollectionRef = firebaseFirestore.collection(FirestoreCollections.USERS)

  override fun observeHistory(
    userId: String,
    limit: Long,
    mangaId: String?,
    lastReadingHistoryId: String?,
  ): Flow<List<ReadingHistoryResponse>> = flow {
    val historyCollectionRef = usersCollectionRef
      .document(userId)
      .collection(FirestoreCollections.HISTORY)

    var query: Query = historyCollectionRef
    mangaId?.let { id -> query = query.whereEqualTo(FirestoreFields.MANGA_ID, id) }
    query = query
      .orderBy(FirestoreFields.CREATED_AT, Query.Direction.DESCENDING)
      .limit(limit)

    lastReadingHistoryId?.let { id ->
      val lastDoc = historyCollectionRef.document(id).get().await() // safe: flow {} is a suspend context
      if (lastDoc.exists()) query = query.startAfter(lastDoc)
    }

    emitAll(
      callbackFlow {
        val listenerRegistration = query.addSnapshotListener { snapshot, error ->
          if (error != null) {
            close(error)
            return@addSnapshotListener
          }

          val readingHistoryResponseList = snapshot?.documents?.mapNotNull { document ->
            document.toObject(ReadingHistoryResponse::class.java)?.copy(id = document.id)
          } ?: emptyList()

          trySend(readingHistoryResponseList)
        }

        awaitClose { listenerRegistration.remove() }
      }
    )
  }

  override suspend fun upsertHistory(
    userId: String,
    readingHistory: ReadingHistoryRequest,
  ) {
    usersCollectionRef
      .document(userId)
      .collection(FirestoreCollections.HISTORY)
      .document(readingHistory.id)
      .set(readingHistory)
      .await()
  }

  override suspend fun removeFromHistory(
    userId: String,
    readingHistoryId: String,
  ) {
    usersCollectionRef
      .document(userId)
      .collection(FirestoreCollections.HISTORY)
      .document(readingHistoryId)
      .delete()
      .await()
  }
}

package com.decoutkhanqindev.dexreader.data.network.firebase.firestore

import com.decoutkhanqindev.dexreader.data.network.firebase.constant.FirestoreCollections
import com.decoutkhanqindev.dexreader.data.network.firebase.constant.FirestoreFields
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.FavoriteMangaRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.ReadingHistoryRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.UserProfileRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.FavoriteMangaResponse
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.ReadingHistoryResponse
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.UserProfileResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreSourceImpl @Inject constructor(
  firebaseFirestore: FirebaseFirestore,
) : FirebaseFirestoreSource {
  private val usersCollectionRef = firebaseFirestore.collection(FirestoreCollections.USERS)

  override suspend fun upsertUserProfile(userProfile: UserProfileRequest) {
    usersCollectionRef
      .document(userProfile.id)
      .set(userProfile)
      .await()
  }

  override fun observeUserProfile(userId: String): Flow<UserProfileResponse?> = callbackFlow {
    val usersDocumentRef = usersCollectionRef.document(userId)

    val listenerRegistration = usersDocumentRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        close(error)
        return@addSnapshotListener
      }

      if (snapshot != null && snapshot.exists()) {
        val userProfileResponse = snapshot.toObject(UserProfileResponse::class.java)
        trySend(userProfileResponse)
      } else {
        trySend(null)
      }
    }

    awaitClose {
      listenerRegistration.remove()
    }
  }

  override fun observeFavorites(
    userId: String,
    limit: Long,
    lastFavoriteMangaId: String?,
  ): Flow<List<FavoriteMangaResponse>> = callbackFlow {
    val favoritesCollectionRef = usersCollectionRef
      .document(userId)
      .collection(FirestoreCollections.FAVORITES)

    var query = favoritesCollectionRef
      .orderBy(FirestoreFields.CREATED_AT, Query.Direction.DESCENDING)
      .limit(limit)

    val lastFavoriteManga = lastFavoriteMangaId?.let { id ->
      favoritesCollectionRef
        .document(id)
        .get()
        .await()
    }

    lastFavoriteManga?.let {
      if (it.exists()) query = query.startAfter(it)
    }

    val listenerRegistration = query.addSnapshotListener { snapshot, error ->
      if (error != null) {
        close(error)
        return@addSnapshotListener
      }

      val favoriteMangaResponseList = snapshot?.documents?.mapNotNull { document ->
        document.toObject(FavoriteMangaResponse::class.java)?.copy(id = document.id)
      } ?: emptyList()

      trySend(favoriteMangaResponseList)
    }

    awaitClose {
      listenerRegistration.remove()
    }
  }

  override suspend fun addToFavorites(
    userId: String,
    manga: FavoriteMangaRequest,
  ) {
    usersCollectionRef
      .document(userId)
      .collection(FirestoreCollections.FAVORITES)
      .document(manga.id)
      .set(manga)
      .await()
  }

  override suspend fun removeFromFavorites(
    userId: String,
    mangaId: String,
  ) {
    usersCollectionRef
      .document(userId)
      .collection(FirestoreCollections.FAVORITES)
      .document(mangaId)
      .delete()
      .await()
  }

  override fun observeIsFavorite(
    userId: String,
    mangaId: String,
  ): Flow<Boolean> = callbackFlow {
    val favoriteCollectionRef = usersCollectionRef
      .document(userId)
      .collection(FirestoreCollections.FAVORITES)
      .document(mangaId)

    val listenerRegistration = favoriteCollectionRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        close(error)
        return@addSnapshotListener
      }

      val isFavorite = snapshot?.exists() ?: false

      trySend(isFavorite)
    }

    awaitClose {
      listenerRegistration.remove()
    }
  }

  override fun observeHistory(
    userId: String,
    limit: Long,
    mangaId: String?,
    lastReadingHistoryId: String?,
  ): Flow<List<ReadingHistoryResponse>> = callbackFlow {
    val historyCollectionRef = usersCollectionRef
      .document(userId)
      .collection(FirestoreCollections.HISTORY)

    var query: Query = historyCollectionRef

    mangaId?.let { id ->
      query = query.whereEqualTo(FirestoreFields.MANGA_ID, id)
    }

    query = query
      .orderBy(FirestoreFields.CREATED_AT, Query.Direction.DESCENDING)
      .limit(limit)

    val lastReadingHistory = lastReadingHistoryId?.let { id ->
      historyCollectionRef
        .document(id)
        .get()
        .await()
    }

    lastReadingHistory?.let {
      if (it.exists()) query = query.startAfter(it)
    }

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

    awaitClose {
      listenerRegistration.remove()
    }
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
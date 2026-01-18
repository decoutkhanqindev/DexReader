package com.decoutkhanqindev.dexreader.data.network.firebase.firestore

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.FavoriteMangaDto
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.ReadingHistoryDto
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.UserProfileDto
import com.decoutkhanqindev.dexreader.di.CreatedAtFieldQualifier
import com.decoutkhanqindev.dexreader.di.FavoritesCollectionQualifier
import com.decoutkhanqindev.dexreader.di.HistoryCollectionQualifier
import com.decoutkhanqindev.dexreader.di.MangaIdFieldQualifier
import com.decoutkhanqindev.dexreader.di.UsersCollectionQualifier
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFirestoreSourceImpl @Inject constructor(
  firebaseFirestore: FirebaseFirestore,
  @param:UsersCollectionQualifier
  private val usersCollection: String,
  @param:FavoritesCollectionQualifier
  private val favoritesCollection: String,
  @param:HistoryCollectionQualifier
  private val historyCollection: String,
  @param:CreatedAtFieldQualifier
  private val createdAtField: String,
  @param:MangaIdFieldQualifier
  private val mangaIdField: String,
) : FirebaseFirestoreSource {
  private val usersCollectionRef = firebaseFirestore.collection(usersCollection)

  override suspend fun addAndUpdateUserProfile(userProfile: UserProfileDto) {
    usersCollectionRef
      .document(userProfile.id)
      .set(userProfile)
      .await()
  }

  override fun observeUserProfile(userId: String): Flow<UserProfileDto?> = callbackFlow {
    val usersDocumentRef = usersCollectionRef.document(userId)

    val listenerRegistration = usersDocumentRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        close(error)
        return@addSnapshotListener
      }

      if (snapshot != null && snapshot.exists()) {
        val userProfileDto = snapshot.toObject(UserProfileDto::class.java)
        trySend(userProfileDto)
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
  ): Flow<List<FavoriteMangaDto>> = callbackFlow {
    val favoritesCollectionRef = usersCollectionRef
      .document(userId)
      .collection(favoritesCollection)

    var query = favoritesCollectionRef
      .orderBy(createdAtField, Query.Direction.DESCENDING)
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

      val favoriteMangaDtoList = snapshot?.documents?.mapNotNull { document ->
        document.toObject(FavoriteMangaDto::class.java)?.copy(id = document.id)
      } ?: emptyList()

      trySend(favoriteMangaDtoList)
    }

    awaitClose {
      listenerRegistration.remove()
    }
  }

  override suspend fun addToFavorites(
    userId: String,
    manga: FavoriteMangaDto,
  ) {
    usersCollectionRef
      .document(userId)
      .collection(favoritesCollection)
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
      .collection(favoritesCollection)
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
      .collection(favoritesCollection)
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
  ): Flow<List<ReadingHistoryDto>> = callbackFlow {
    val historyCollectionRef = usersCollectionRef
      .document(userId)
      .collection(historyCollection)

    var query: Query = historyCollectionRef

    mangaId?.let { id ->
      query = query.whereEqualTo(mangaIdField, id)
    }

    query = query
      .orderBy(createdAtField, Query.Direction.DESCENDING)
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

      val readingHistoryDtoList = snapshot?.documents?.mapNotNull { document ->
        document.toObject(ReadingHistoryDto::class.java)?.copy(id = document.id)
      } ?: emptyList()

      trySend(readingHistoryDtoList)
    }

    awaitClose {
      listenerRegistration.remove()
    }
  }

  override suspend fun addAndUpdateToHistory(
    userId: String,
    readingHistory: ReadingHistoryDto,
  ) {
    usersCollectionRef
      .document(userId)
      .collection(historyCollection)
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
      .collection(historyCollection)
      .document(readingHistoryId)
      .delete()
      .await()
  }
}


package com.decoutkhanqindev.dexreader.data.network.firebase.firestore.favorite

import com.decoutkhanqindev.dexreader.data.network.firebase.constant.FirestoreCollections
import com.decoutkhanqindev.dexreader.data.network.firebase.constant.FirestoreFields
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.FavoriteMangaRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.FavoriteMangaResponse
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFavoriteFirestoreSourceImpl @Inject constructor(
  firebaseFirestore: FirebaseFirestore,
) : FirebaseFavoriteFirestoreSource {
  private val usersCollectionRef = firebaseFirestore.collection(FirestoreCollections.USERS)

  override fun observeFavorites(
    userId: String,
    limit: Long,
    lastFavoriteMangaId: String?,
  ): Flow<List<FavoriteMangaResponse>> = flow {
    val favoritesCollectionRef = usersCollectionRef
      .document(userId)
      .collection(FirestoreCollections.FAVORITES)

    var query = favoritesCollectionRef
      .orderBy(FirestoreFields.CREATED_AT, Query.Direction.DESCENDING)
      .limit(limit)

    lastFavoriteMangaId?.let { id ->
      val lastDoc = favoritesCollectionRef.document(id).get().await()
      if (lastDoc.exists()) query = query.startAfter(lastDoc)
    }

    emitAll(
      callbackFlow {
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

        awaitClose { listenerRegistration.remove() }
      }
    )
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
}

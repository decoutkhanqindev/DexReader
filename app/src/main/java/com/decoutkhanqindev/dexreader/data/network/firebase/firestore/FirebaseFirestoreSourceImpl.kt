package com.decoutkhanqindev.dexreader.data.network.firebase.firestore

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.FavoriteMangaDto
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.UserProfileDto
import com.decoutkhanqindev.dexreader.di.FavoritesCollectionQualifier
import com.decoutkhanqindev.dexreader.di.HistoryCollectionQualifier
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
  @UsersCollectionQualifier
  private val usersCollection: String,
  @FavoritesCollectionQualifier
  private val favoritesCollection: String,
  @HistoryCollectionQualifier
  private val historyCollection: String
) : FirebaseFirestoreSource {
  private val usersCollectionRef = firebaseFirestore.collection(usersCollection)

  override suspend fun addUserProfile(userProfile: UserProfileDto): UserProfileDto {
    val documentRef = usersCollectionRef.document(userProfile.id)
    documentRef.set(userProfile).await()
    return userProfile
  }

  override fun observeUserProfile(userId: String): Flow<UserProfileDto?> = callbackFlow {
    val documentRef = usersCollectionRef.document(userId)

    val listenerRegistration = documentRef.addSnapshotListener { snapshot, error ->
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

  override suspend fun updateUserProfile(userProfile: UserProfileDto) {
    usersCollectionRef.document(userProfile.id).set(userProfile).await()
  }

  override fun observeFavorites(userId: String): Flow<List<FavoriteMangaDto>> = callbackFlow {
    val favoritesCollectionRef = usersCollectionRef
      .document(userId)
      .collection(favoritesCollection)
      .orderBy("added_at", Query.Direction.DESCENDING)

    val listenerRegistration = favoritesCollectionRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        close(error)
        return@addSnapshotListener
      }

      val favoriteMangaDtoList = snapshot?.documents?.mapNotNull { document ->
        document.toObject(FavoriteMangaDto::class.java)
      } ?: emptyList()

      trySend(favoriteMangaDtoList)
    }

    awaitClose {
      listenerRegistration.remove()
    }
  }

  override suspend fun addToFavorites(userId: String, manga: FavoriteMangaDto) {
    usersCollectionRef
      .document(userId)
      .collection(favoritesCollection)
      .document(manga.id)
      .set(manga)
      .await()
  }

  override suspend fun removeFromFavorites(userId: String, mangaId: String) {
    usersCollectionRef
      .document(userId)
      .collection(favoritesCollection)
      .document(mangaId)
      .delete()
      .await()
  }

  override fun observeIsFavorite(userId: String, mangaId: String): Flow<Boolean> = callbackFlow {
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
}


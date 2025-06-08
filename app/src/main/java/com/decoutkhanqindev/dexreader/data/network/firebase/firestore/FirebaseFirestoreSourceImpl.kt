package com.decoutkhanqindev.dexreader.data.network.firebase.firestore

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.UserProfileDto
import com.decoutkhanqindev.dexreader.di.FavoritesCollectionQualifier
import com.decoutkhanqindev.dexreader.di.HistoryCollectionQualifier
import com.decoutkhanqindev.dexreader.di.UsersCollectionQualifier
import com.google.firebase.firestore.FirebaseFirestore
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
  // private val favoritesCollectionRef = firebaseFirestore.collection(favoritesCollection)
  // private val historyCollectionRef = firebaseFirestore.collection(historyCollection)

  override suspend fun addUserProfile(userProfile: UserProfileDto) {
    usersCollectionRef.document(userProfile.id).set(userProfile).await()
  }

  override fun observeUserProfile(userId: String): Flow<UserProfileDto?> = callbackFlow {
    val documentRef = usersCollectionRef.document(userId)

    val listenerRegistration = documentRef.addSnapshotListener { snapshot, error ->
      if (error != null) {
        trySend(null)
        close(error)
        return@addSnapshotListener
      }

      if (snapshot != null && snapshot.exists()) {
        val userProfile = snapshot.toObject(UserProfileDto::class.java)
        trySend(userProfile)
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

  companion object {
    private const val USERS_COLLECTION = "users"
  }
}


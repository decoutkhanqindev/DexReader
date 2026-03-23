package com.decoutkhanqindev.dexreader.data.network.firebase.firestore.user

import com.decoutkhanqindev.dexreader.data.network.firebase.constant.FirestoreCollections
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.request.UserProfileRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.UserProfileResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserFirestoreSourceImpl @Inject constructor(
  firebaseFirestore: FirebaseFirestore,
) : FirebaseUserFirestoreSource {
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
}

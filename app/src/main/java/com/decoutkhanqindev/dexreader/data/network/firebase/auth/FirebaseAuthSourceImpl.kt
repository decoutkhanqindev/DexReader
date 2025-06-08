package com.decoutkhanqindev.dexreader.data.network.firebase.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthSourceImpl @Inject constructor(
  private val firebaseAuth: FirebaseAuth
) : FirebaseAuthSource {
  override suspend fun registerUser(email: String, password: String) {
    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
  }

  override suspend fun loginUser(email: String, password: String) {
    firebaseAuth.signInWithEmailAndPassword(email, password).await()
  }

  override suspend fun logoutUser() = firebaseAuth.signOut()

  override suspend fun resetUserPassword(email: String) {
    firebaseAuth.sendPasswordResetEmail(email).await()
  }

  override fun observeCurrentUser(): Flow<FirebaseUser?> = callbackFlow {
    val authStateListener = FirebaseAuth.AuthStateListener { auth ->
      trySend(auth.currentUser)
    }

    firebaseAuth.addAuthStateListener(authStateListener)

    awaitClose {
      firebaseAuth.removeAuthStateListener(authStateListener)
    }
  }
}
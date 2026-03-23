package com.decoutkhanqindev.dexreader.data.network.firebase.auth

import com.decoutkhanqindev.dexreader.data.mapper.UserMapper.toUser
import com.decoutkhanqindev.dexreader.domain.entity.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthSourceImpl @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
) : FirebaseAuthSource {
  override suspend fun register(
    email: String,
    password: String,
  ): User? {
    val result = firebaseAuth.createUserWithEmailAndPassword(
      email,
      password
    ).await()
    return result.user?.toUser()
  }

  override suspend fun login(
    email: String,
    password: String,
  ) {
    firebaseAuth.signInWithEmailAndPassword(
      email,
      password
    ).await()
  }

  override suspend fun logout() = firebaseAuth.signOut()

  override suspend fun deleteCurrentUser() {
    firebaseAuth.currentUser?.delete()?.await()
  }

  override suspend fun sendResetPassword(email: String) {
    firebaseAuth.sendPasswordResetEmail(email).await()
  }

  override fun observeCurrentUser(): Flow<User?> = callbackFlow {
    val authStateListener = FirebaseAuth.AuthStateListener { auth ->
      trySend(auth.currentUser?.toUser())
    }

    firebaseAuth.addAuthStateListener(authStateListener)

    awaitClose {
      firebaseAuth.removeAuthStateListener(authStateListener)
    }
  }
}
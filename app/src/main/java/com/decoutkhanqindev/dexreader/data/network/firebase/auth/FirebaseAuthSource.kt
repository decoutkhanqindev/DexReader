package com.decoutkhanqindev.dexreader.data.network.firebase.auth

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthSource {
  suspend fun register(email: String, password: String): FirebaseUser?
  suspend fun login(email: String, password: String)
  suspend fun logout()
  suspend fun sendResetPassword(email: String)
  fun observeCurrentUser(): Flow<FirebaseUser?>
}
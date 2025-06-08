package com.decoutkhanqindev.dexreader.data.network.firebase.auth

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthSource {
  suspend fun registerUser(email: String, password: String)
  suspend fun loginUser(email: String, password: String)
  suspend fun logoutUser()
  suspend fun resetUserPassword(email: String)
  fun observeCurrentUser(): Flow<FirebaseUser?>
}
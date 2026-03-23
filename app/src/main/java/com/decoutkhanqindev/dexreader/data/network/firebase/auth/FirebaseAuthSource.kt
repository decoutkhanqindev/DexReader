package com.decoutkhanqindev.dexreader.data.network.firebase.auth

import com.decoutkhanqindev.dexreader.domain.entity.user.User
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthSource {
  suspend fun register(email: String, password: String): User?
  suspend fun login(email: String, password: String)
  suspend fun logout()
  suspend fun deleteCurrentUser()
  suspend fun sendResetPassword(email: String)
  fun observeCurrentUser(): Flow<User?>
}
package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
  // authentication methods
  suspend fun register(email: String, password: String, name: String)
  suspend fun login(email: String, password: String)
  suspend fun logout()
  suspend fun sendResetPassword(email: String)
  fun observeCurrentUser(): Flow<User?>

  // user profile methods
  suspend fun updateUserProfile(user: User)
  fun observeUserProfile(userId: String): Flow<User?>
}
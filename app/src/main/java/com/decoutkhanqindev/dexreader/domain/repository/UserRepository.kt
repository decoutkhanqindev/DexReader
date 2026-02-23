package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
  // authentication methods
  suspend fun registerUser(email: String, password: String): User
  suspend fun loginUser(email: String, password: String)
  suspend fun logoutUser()
  suspend fun sendResetUserPassword(email: String)
  fun observeCurrentUser(): Flow<User?>

  // user profile methods
  suspend fun addAndUpdateUserProfile(user: User)
  fun observeUserProfile(userId: String): Flow<User?>
}
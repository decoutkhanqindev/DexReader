package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
  // authentication methods
  suspend fun registerUser(email: String, password: String): Result<User>
  suspend fun loginUser(email: String, password: String): Result<Unit>
  suspend fun logoutUser(): Result<Unit>
  suspend fun resetUserPassword(email: String): Result<Unit>
  fun observeCurrentUser(): Flow<Result<User?>>

  // user profile methods
  suspend fun addUserProfile(user: User): Result<User>
  fun observeUserProfile(userId: String): Flow<Result<User?>>
  suspend fun updateUserProfile(user: User): Result<Unit>
}
package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.toDomain
import com.decoutkhanqindev.dexreader.data.mapper.toUserProfileDto
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.decoutkhanqindev.dexreader.utils.runSuspendCatching
import com.decoutkhanqindev.dexreader.utils.toResultFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
  private val firebaseAuthSource: FirebaseAuthSource,
  private val firebaseFirestoreSource: FirebaseFirestoreSource
) : UserRepository {
  override suspend fun registerUser(email: String, password: String): Result<User> =
    runSuspendCatching(Dispatchers.IO) {
      firebaseAuthSource.registerUser(email, password)?.toDomain()
        ?: throw IllegalStateException("User registration failed")
    }

  override suspend fun loginUser(email: String, password: String): Result<Unit> =
    runSuspendCatching(Dispatchers.IO) {
      firebaseAuthSource.loginUser(email, password)
    }

  override suspend fun logoutUser(): Result<Unit> =
    runSuspendCatching(Dispatchers.IO) {
      firebaseAuthSource.logoutUser()
    }

  override suspend fun resetUserPassword(email: String): Result<Unit> =
    runSuspendCatching(Dispatchers.IO) {
      firebaseAuthSource.resetUserPassword(email)
    }

  override fun observeCurrentUser(): Flow<Result<User?>> =
    firebaseAuthSource.observeCurrentUser()
      .map { it?.toDomain() }
      .flowOn(Dispatchers.IO)
      .toResultFlow()

  override suspend fun addUserProfile(user: User): Result<User> =
    runSuspendCatching(Dispatchers.IO) {
      firebaseFirestoreSource.addUserProfile(user.toUserProfileDto()).toDomain()
    }

  override fun observeUserProfile(userId: String): Flow<Result<User?>> =
    firebaseFirestoreSource.observeUserProfile(userId)
      .map { it?.toDomain() }
      .flowOn(Dispatchers.IO)
      .toResultFlow()

  override suspend fun updateUserProfile(user: User): Result<Unit> =
    runSuspendCatching(Dispatchers.IO) {
      firebaseFirestoreSource.updateUserProfile(user.toUserProfileDto())
    }
}
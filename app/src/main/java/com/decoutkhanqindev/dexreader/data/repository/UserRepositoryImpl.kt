package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.toDomain
import com.decoutkhanqindev.dexreader.data.mapper.toUserProfileDto
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
  private val firebaseAuthSource: FirebaseAuthSource,
  private val firebaseFirestoreSource: FirebaseFirestoreSource,
) : UserRepository {
  override suspend fun registerUser(
    email: String,
    password: String,
  ): User = withContext(Dispatchers.IO) {
    firebaseAuthSource.registerUser(email, password)?.toDomain()
      ?: throw IllegalStateException("User registration failed")
  }

  override suspend fun loginUser(
    email: String,
    password: String,
  ) = withContext(Dispatchers.IO) {
    firebaseAuthSource.loginUser(email, password)
  }

  override suspend fun logoutUser() =
    withContext(Dispatchers.IO) {
      firebaseAuthSource.logoutUser()
    }

  override suspend fun sendResetUserPassword(email: String) =
    withContext(Dispatchers.IO) {
      firebaseAuthSource.sendResetUserPassword(email)
    }

  override fun observeCurrentUser(): Flow<User?> =
    firebaseAuthSource
      .observeCurrentUser()
      .map { it?.toDomain() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun addAndUpdateUserProfile(user: User) =
    withContext(Dispatchers.IO) {
      firebaseFirestoreSource.addAndUpdateUserProfile(userProfile = user.toUserProfileDto())
    }

  override fun observeUserProfile(userId: String): Flow<User?> =
    firebaseFirestoreSource
      .observeUserProfile(userId)
      .map { it?.toDomain() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()
}

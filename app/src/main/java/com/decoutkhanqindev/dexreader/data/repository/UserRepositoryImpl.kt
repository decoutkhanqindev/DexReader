package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.toDomain
import com.decoutkhanqindev.dexreader.data.mapper.toUserProfileDto
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.domain.exception.AuthException
import com.decoutkhanqindev.dexreader.domain.model.User
import com.decoutkhanqindev.dexreader.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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
  override suspend fun register(
    email: String,
    password: String,
  ) = withContext(Dispatchers.IO) {
    try {
      val registeredUser = firebaseAuthSource.register(email, password)?.toDomain()
        ?: throw AuthException.RegistrationFailed()
      firebaseFirestoreSource.upsertUserProfile(userProfile = registeredUser.toUserProfileDto())
    } catch (e: Exception) {
      when (e) {
        is FirebaseAuthUserCollisionException -> throw AuthException.UserAlreadyExists(cause = e)
        else -> throw e
      }
    }
  }

  override suspend fun login(
    email: String,
    password: String,
  ) = withContext(Dispatchers.IO) {
    try {
      firebaseAuthSource.login(email, password)
    } catch (e: Exception) {
      when (e) {
        is FirebaseAuthInvalidUserException -> throw AuthException.UserNotFound(cause = e)
        is FirebaseAuthInvalidCredentialsException -> throw AuthException.Password.Incorrect(cause = e)
        else -> throw e
      }
    }
  }

  override suspend fun logout() = withContext(Dispatchers.IO) { firebaseAuthSource.logout() }

  override suspend fun sendResetPassword(email: String) =
    withContext(Dispatchers.IO) { firebaseAuthSource.sendResetPassword(email) }

  override fun observeCurrentUser(): Flow<User?> =
    firebaseAuthSource
      .observeCurrentUser()
      .map { it?.toDomain() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun updateUserProfile(user: User) =
    withContext(Dispatchers.IO) {
      firebaseFirestoreSource.upsertUserProfile(userProfile = user.toUserProfileDto())
    }

  override fun observeUserProfile(userId: String): Flow<User?> =
    firebaseFirestoreSource
      .observeUserProfile(userId)
      .map { it?.toDomain() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()
}

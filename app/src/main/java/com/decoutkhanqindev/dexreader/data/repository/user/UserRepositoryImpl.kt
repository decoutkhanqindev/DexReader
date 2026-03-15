package com.decoutkhanqindev.dexreader.data.repository.user

import com.decoutkhanqindev.dexreader.data.mapper.UserMapper.toUser
import com.decoutkhanqindev.dexreader.data.mapper.UserMapper.toUserProfileRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.exception.InfrastructureException
import com.decoutkhanqindev.dexreader.domain.model.user.User
import com.decoutkhanqindev.dexreader.domain.repository.user.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
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

class UserRepositoryImpl
@Inject
constructor(
  private val firebaseAuthSource: FirebaseAuthSource,
  private val firebaseFirestoreSource: FirebaseFirestoreSource,
) : UserRepository {
  override suspend fun register(
    email: String,
    password: String,
    name: String,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        val registeredUser =
          firebaseAuthSource
            .register(email, password)
            ?.toUser()
            ?.copy(name = name)
            ?: throw BusinessException.Auth.RegistrationFailed()
        firebaseFirestoreSource.upsertUserProfile(
          userProfile = registeredUser.toUserProfileRequest()
        )
      },
      onCatch = { e ->
        when (e) {
          is FirebaseAuthUserCollisionException ->
            throw BusinessException.Auth.UserAlreadyExists(cause = e)

          else -> throw InfrastructureException.Unexpected(cause = e)
        }
      }
    )

  override suspend fun login(
    email: String,
    password: String,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { firebaseAuthSource.login(email, password) },
      onCatch = { e ->
        when (e) {
          is FirebaseAuthInvalidUserException ->
            throw BusinessException.Auth.UserNotFound(cause = e)

          is FirebaseAuthInvalidCredentialsException ->
            throw BusinessException.Auth.InvalidCredentials(cause = e)

          else -> throw InfrastructureException.Unexpected(cause = e)
        }
      }
    )

  override suspend fun logout() = withContext(Dispatchers.IO) { firebaseAuthSource.logout() }

  override suspend fun sendResetPassword(email: String) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { firebaseAuthSource.sendResetPassword(email) },
      onCatch = { e ->
        when (e) {
          is BusinessException.Auth -> throw e
          is FirebaseAuthInvalidUserException ->
            throw BusinessException.Auth.UserNotFound(cause = e)

          else -> throw InfrastructureException.Unexpected(cause = e)
        }
      }
    )

  override fun observeCurrentUser(): Flow<User?> =
    firebaseAuthSource
      .observeCurrentUser()
      .map { it?.toUser() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun updateUserProfile(user: User) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        firebaseFirestoreSource.upsertUserProfile(
          userProfile = user.toUserProfileRequest()
        )
      },
      onCatch = { e ->
        when (e) {
          is BusinessException.Auth -> throw e
          else -> throw InfrastructureException.Unexpected(cause = e)
        }
      }
    )

  override fun observeUserProfile(userId: String): Flow<User?> =
    firebaseFirestoreSource
      .observeUserProfile(userId)
      .map { it?.toUser() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()
}

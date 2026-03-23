package com.decoutkhanqindev.dexreader.data.repository.user

import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toAuthException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toAuthFlowException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirestoreException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirestoreFlowException
import com.decoutkhanqindev.dexreader.data.mapper.UserMapper.toUser
import com.decoutkhanqindev.dexreader.data.mapper.UserMapper.toUserProfileRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.FirebaseFirestoreSource
import com.decoutkhanqindev.dexreader.domain.entity.user.User
import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.exception.InfrastructureException
import com.decoutkhanqindev.dexreader.domain.repository.user.UserRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
  private val authSource: FirebaseAuthSource,
  private val firestoreSource: FirebaseFirestoreSource,
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
          authSource
            .register(email, password)
            ?.toUser()
            ?.copy(name = name)
            ?: throw BusinessException.Auth.RegistrationFailed()
        try {
          firestoreSource.upsertUserProfile(
            userProfile = registeredUser.toUserProfileRequest()
          )
        } catch (c: CancellationException) {
          throw c
        } catch (e: Exception) {
          // Swallow logout failure — the original Firestore write failure is the one to surface.
          try {
            authSource.logout()
          } catch (_: Exception) {
          }

          throw InfrastructureException.Unexpected(rootCause = e)
        }
      },
      onCatch = { e -> e.toAuthException() }
    )

  override suspend fun login(
    email: String,
    password: String,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { authSource.login(email, password) },
      onCatch = { e -> e.toAuthException() }
    )

  override suspend fun logout() = withContext(Dispatchers.IO) { authSource.logout() }

  override suspend fun sendResetPassword(email: String) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { authSource.sendResetPassword(email) },
      onCatch = { e -> e.toAuthException() }
    )

  override fun observeCurrentUser(): Flow<User?> =
    authSource
      .observeCurrentUser()
      .map { it?.toUser() }
      .flowOn(Dispatchers.IO)
      .catch { e -> e.toAuthFlowException() }
      .distinctUntilChanged()

  override suspend fun updateUserProfile(user: User) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        firestoreSource.upsertUserProfile(
          userProfile = user.toUserProfileRequest()
        )
      },
      onCatch = { e -> e.toFirestoreException() }
    )

  override fun observeUserProfile(userId: String): Flow<User?> =
    firestoreSource
      .observeUserProfile(userId)
      .map { it?.toUser() }
      .flowOn(Dispatchers.IO)
      .catch { e -> e.toFirestoreFlowException() }
      .distinctUntilChanged()
}

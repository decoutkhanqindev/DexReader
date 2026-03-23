package com.decoutkhanqindev.dexreader.data.repository.user

import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseAuthException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreFlowException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toUnexpectedException
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
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

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
          firestoreSource.upsertUserProfile(userProfile = registeredUser.toUserProfileRequest())
        } catch (c: CancellationException) {
          throw c
        } catch (e: Exception) {
          // Best-effort rollback: delete the auth account so the user can retry registration.
          // Swallow deletion failure — the original Firestore write failure is the one to surface.
          try {
            authSource.deleteCurrentUser()
          } catch (c: CancellationException) {
            throw c
          } catch (_: Exception) {
          }

          throw InfrastructureException.Unexpected(cause = e)
        }
      },
      onCatch = { e -> e.toFirebaseAuthException() }
    )

  override suspend fun login(
    email: String,
    password: String,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { authSource.login(email, password) },
      onCatch = { e -> e.toFirebaseAuthException() }
    )

  override suspend fun logout() =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { authSource.logout() },
      onCatch = { e -> e.toFirebaseAuthException() }
    )

  override suspend fun sendResetPassword(email: String) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = { authSource.sendResetPassword(email) },
      onCatch = { e -> e.toFirebaseAuthException() }
    )

  override fun observeCurrentUser(): Flow<User?> =
    authSource
      .observeCurrentUser()
      .map { it?.toUser() }
      .catch { e -> e.toUnexpectedException() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun updateUserProfile(user: User) =
    runSuspendCatching(
      context = Dispatchers.IO,
      onExecute = {
        firestoreSource.upsertUserProfile(
          userProfile = user.toUserProfileRequest()
        )
      },
      onCatch = { e -> e.toFirebaseFirestoreException() }
    )

  override fun observeUserProfile(userId: String): Flow<User?> =
    firestoreSource
      .observeUserProfile(userId)
      .map { it?.toUser() }
      .catch { e -> e.toFirebaseFirestoreFlowException() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()
}

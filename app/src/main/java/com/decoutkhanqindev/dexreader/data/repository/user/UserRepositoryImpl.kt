package com.decoutkhanqindev.dexreader.data.repository.user

import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseAuthException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toFirebaseFirestoreFlowException
import com.decoutkhanqindev.dexreader.data.mapper.ExceptionMapper.toUnexpectedException
import com.decoutkhanqindev.dexreader.data.mapper.UserMapper.toUser
import com.decoutkhanqindev.dexreader.data.mapper.UserMapper.toUserProfileRequest
import com.decoutkhanqindev.dexreader.data.network.firebase.auth.FirebaseAuthSource
import com.decoutkhanqindev.dexreader.data.network.firebase.firestore.user.FirebaseUserFirestoreSource
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
  private val firestoreSource: FirebaseUserFirestoreSource,
) : UserRepository {
  override suspend fun register(
    email: String,
    password: String,
    name: String,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        val registeredUser =
          authSource
            .register(email, password)
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
      catch = { e -> e.toFirebaseAuthException() }
    )

  override suspend fun login(
    email: String,
    password: String,
  ) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = { authSource.login(email, password) },
      catch = { e -> e.toFirebaseAuthException() }
    )

  override suspend fun logout() =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = { authSource.logout() },
      catch = { e -> e.toFirebaseAuthException() }
    )

  override suspend fun sendResetPassword(email: String) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = { authSource.sendResetPassword(email) },
      catch = { e -> e.toFirebaseAuthException() }
    )

  override fun observeCurrentUser(): Flow<User?> =
    authSource
      .observeCurrentUser()
      .catch { e -> e.toUnexpectedException() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()

  override suspend fun updateUserProfile(user: User) =
    runSuspendCatching(
      context = Dispatchers.IO,
      block = {
        firestoreSource.upsertUserProfile(
          userProfile = user.toUserProfileRequest()
        )
      },
      catch = { e -> e.toFirebaseFirestoreException() }
    )

  override fun observeUserProfile(userId: String): Flow<User?> =
    firestoreSource
      .observeUserProfile(userId)
      .map { it?.toUser() }
      .catch { e -> e.toFirebaseFirestoreFlowException() }
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()
}

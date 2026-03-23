package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.exception.DomainException
import com.decoutkhanqindev.dexreader.domain.exception.InfrastructureException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestoreException
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

object ExceptionMapper {

  fun Exception.toDomainException(): Nothing =
    when (this) {
      is DomainException -> throw this
      is HttpException -> throw InfrastructureException.ServerUnavailable(rootCause = this)
      is IOException -> throw InfrastructureException.NetworkUnavailable(rootCause = this)
      else -> throw InfrastructureException.Unexpected(rootCause = this)
    }

  fun Exception.toFirestoreException(): Nothing =
    when (this) {
      is DomainException -> throw this
      is FirebaseFirestoreException ->
        if (code == FirebaseFirestoreException.Code.PERMISSION_DENIED)
          throw BusinessException.Resource.AccessDenied(rootCause = this)
        else throw InfrastructureException.Unexpected(rootCause = this)

      else -> throw InfrastructureException.Unexpected(rootCause = this)
    }

  fun Exception.toCacheException(): Nothing =
    when (this) {
      is DomainException -> throw this
      else -> throw InfrastructureException.Unexpected(rootCause = this)
    }

  /**
   * Maps Firebase Auth exceptions to domain [BusinessException.Auth] subtypes.
   * Used exclusively in [UserRepository] operations (register, login, sendResetPassword).
   *
   * - [FirebaseAuthUserCollisionException] → [BusinessException.Auth.UserAlreadyExists]
   * - [FirebaseAuthInvalidUserException]   → [BusinessException.Auth.UserNotFound]
   * - [FirebaseAuthInvalidCredentialsException] → [BusinessException.Auth.InvalidCredentials]
   * - [DomainException] → rethrown unchanged
   * - Everything else  → [InfrastructureException.Unexpected]
   */
  fun Exception.toAuthException(): Nothing =
    when (this) {
      is DomainException -> throw this
      is FirebaseAuthUserCollisionException ->
        throw BusinessException.Auth.UserAlreadyExists(rootCause = this)
      is FirebaseAuthInvalidUserException ->
        throw BusinessException.Auth.UserNotFound(rootCause = this)
      is FirebaseAuthInvalidCredentialsException ->
        throw BusinessException.Auth.InvalidCredentials(rootCause = this)
      else -> throw InfrastructureException.Unexpected(rootCause = this)
    }

  /**
   * For use inside Flow `.catch { }` blocks, whose lambda receives [Throwable] not [Exception].
   * Mirrors [toFirestoreException]: maps PERMISSION_DENIED → [BusinessException.Resource.AccessDenied],
   * other [FirebaseFirestoreException] → [InfrastructureException.Unexpected],
   * [DomainException] and [CancellationException]-derived throwables are rethrown unchanged.
   */
  fun Throwable.toFirestoreFlowException(): Nothing =
    when (this) {
      is FirebaseFirestoreException if code == FirebaseFirestoreException.Code.PERMISSION_DENIED ->
        throw BusinessException.Resource.AccessDenied(rootCause = this)
      is FirebaseFirestoreException -> throw InfrastructureException.Unexpected(rootCause = this)
      else -> throw this
    }

  /**
   * For use inside Flow `.catch { }` blocks on auth state streams.
   * [CancellationException] and [DomainException] are rethrown unchanged so structured
   * concurrency is preserved. Everything else is wrapped as [InfrastructureException.Unexpected].
   */
  fun Throwable.toAuthFlowException(): Nothing =
    when (this) {
      is CancellationException -> throw this
      is DomainException -> throw this
      else -> throw InfrastructureException.Unexpected(rootCause = this)
    }
}

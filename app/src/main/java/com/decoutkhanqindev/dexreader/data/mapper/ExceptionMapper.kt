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
      is HttpException ->
        if (code() >= 500) throw InfrastructureException.ServerUnavailable(cause = this)
        else throw InfrastructureException.Unexpected(cause = this)
      is IOException -> throw InfrastructureException.NetworkUnavailable(cause = this)
      else -> throw InfrastructureException.Unexpected(cause = this)
    }

  fun Throwable.toUnexpectedException(): Nothing =
    when (this) {
      is CancellationException -> throw this
      is DomainException -> throw this
      else -> throw InfrastructureException.Unexpected(cause = this)
    }

  fun Exception.toFirebaseFirestoreException(): Nothing =
    when (this) {
      is DomainException -> throw this
      is FirebaseFirestoreException ->
        if (code == FirebaseFirestoreException.Code.PERMISSION_DENIED)
          throw BusinessException.Resource.AccessDenied(cause = this)
        else throw InfrastructureException.Unexpected(cause = this)
      else -> throw InfrastructureException.Unexpected(cause = this)
    }

  fun Throwable.toFirebaseFirestoreFlowException(): Nothing =
    when (this) {
      is CancellationException -> throw this
      is FirebaseFirestoreException if code == FirebaseFirestoreException.Code.PERMISSION_DENIED ->
        throw BusinessException.Resource.AccessDenied(cause = this)
      is FirebaseFirestoreException
        if (code == FirebaseFirestoreException.Code.UNAVAILABLE ||
            code == FirebaseFirestoreException.Code.DEADLINE_EXCEEDED) ->
        throw InfrastructureException.NetworkUnavailable(cause = this)
      is FirebaseFirestoreException -> throw InfrastructureException.Unexpected(cause = this)
      else -> throw this
    }

  fun Exception.toFirebaseAuthException(): Nothing =
    when (this) {
      is DomainException -> throw this
      is FirebaseAuthUserCollisionException ->
        throw BusinessException.Auth.UserAlreadyExists(cause = this)
      is FirebaseAuthInvalidUserException ->
        throw BusinessException.Auth.UserNotFound(cause = this)
      is FirebaseAuthInvalidCredentialsException ->
        throw BusinessException.Auth.InvalidCredentials(cause = this)
      else -> throw InfrastructureException.Unexpected(cause = this)
    }
}

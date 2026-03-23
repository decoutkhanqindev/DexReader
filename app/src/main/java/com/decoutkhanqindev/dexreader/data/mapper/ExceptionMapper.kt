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

object ExceptionMapper {

  fun Exception.toDomainException(): Nothing =
    when (this) {
      is DomainException -> throw this
      is HttpException -> throw InfrastructureException.ServerUnavailable(cause = this)
      is IOException -> throw InfrastructureException.NetworkUnavailable(cause = this)
      else -> throw InfrastructureException.Unexpected(cause = this)
    }

  fun Throwable.toUnexpectedException(): Nothing =
    when (this) {
      is DomainException -> throw this
      else -> throw InfrastructureException.Unexpected(cause = this)
    }

  fun Exception.toFirestoreException(): Nothing =
    when (this) {
      is DomainException -> throw this
      is FirebaseFirestoreException ->
        if (code == FirebaseFirestoreException.Code.PERMISSION_DENIED)
          throw BusinessException.Resource.AccessDenied(cause = this)
        else throw InfrastructureException.Unexpected(cause = this)
      else -> throw InfrastructureException.Unexpected(cause = this)
    }

  fun Throwable.toFirestoreFlowException(): Nothing =
    when (this) {
      is FirebaseFirestoreException if code == FirebaseFirestoreException.Code.PERMISSION_DENIED ->
        throw BusinessException.Resource.AccessDenied(cause = this)
      is FirebaseFirestoreException -> throw InfrastructureException.Unexpected(cause = this)
      else -> throw this
    }

  fun Exception.toAuthException(): Nothing =
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

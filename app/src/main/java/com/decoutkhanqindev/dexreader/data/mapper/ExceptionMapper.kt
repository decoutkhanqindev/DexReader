package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.exception.DomainException
import com.decoutkhanqindev.dexreader.domain.exception.InfrastructureException
import com.google.firebase.firestore.FirebaseFirestoreException
import retrofit2.HttpException
import java.io.IOException

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
   * For use inside Flow `.catch { }` blocks, whose lambda receives [Throwable] not [Exception].
   * Maps Firestore PERMISSION_DENIED to [BusinessException.Resource.AccessDenied]; rethrows all others.
   */
  fun Throwable.toFirestoreFlowException(): Nothing {
    if (this is FirebaseFirestoreException &&
      code == FirebaseFirestoreException.Code.PERMISSION_DENIED
    ) throw BusinessException.Resource.AccessDenied(rootCause = this)
    throw this
  }
}

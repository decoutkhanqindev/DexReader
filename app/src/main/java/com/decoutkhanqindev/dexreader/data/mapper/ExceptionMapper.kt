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
      is HttpException -> throw InfrastructureException.ServerUnavailable(cause = this)
      is IOException -> throw InfrastructureException.NetworkUnavailable(cause = this)
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

  fun Exception.toCacheException(): Nothing =
    when (this) {
      is DomainException -> throw this
      else -> throw InfrastructureException.Unexpected(cause = this)
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
        throw BusinessException.Resource.AccessDenied(cause = this)
      is FirebaseFirestoreException -> throw InfrastructureException.Unexpected(cause = this)
      else -> throw this
    }
}

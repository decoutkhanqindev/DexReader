package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.domain.exception.DomainException
import com.decoutkhanqindev.dexreader.domain.exception.FavoritesException
import com.decoutkhanqindev.dexreader.domain.exception.HistoryException
import com.google.firebase.firestore.FirebaseFirestoreException
import retrofit2.HttpException
import java.io.IOException

object ExceptionMapper {

  fun Exception.toApiDomainException(): Nothing =
    when (this) {
      is DomainException -> throw this
      is HttpException -> throw DomainException.ServiceRequestFailed(cause = this)
      is IOException -> throw DomainException.NetworkUnavailable(cause = this)
      else -> throw DomainException.Unknown(cause = this)
    }

  fun Exception.toFavoritesDomainException(): Nothing =
    when (this) {
      is FavoritesException -> throw this
      is FirebaseFirestoreException ->
        if (code == FirebaseFirestoreException.Code.PERMISSION_DENIED)
          throw FavoritesException.PermissionDenied(cause = this)
        else throw DomainException.Unknown(cause = this)

      else -> throw DomainException.Unknown(cause = this)
    }

  fun Exception.toHistoryDomainException(): Nothing =
    when (this) {
      is HistoryException -> throw this
      is FirebaseFirestoreException ->
        if (code == FirebaseFirestoreException.Code.PERMISSION_DENIED)
          throw HistoryException.PermissionDenied(cause = this)
        else throw DomainException.Unknown(cause = this)

      else -> throw DomainException.Unknown(cause = this)
    }

  fun Exception.toCacheDomainException(): Nothing =
    when (this) {
      is DomainException -> throw this
      else -> throw DomainException.Unknown(cause = this)
    }
}

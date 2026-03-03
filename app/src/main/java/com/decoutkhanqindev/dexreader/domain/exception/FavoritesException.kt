package com.decoutkhanqindev.dexreader.domain.exception

sealed class FavoritesException(cause: Throwable? = null) : DomainException(cause = cause) {
  class PermissionDenied(cause: Throwable? = null) : FavoritesException(cause)
}

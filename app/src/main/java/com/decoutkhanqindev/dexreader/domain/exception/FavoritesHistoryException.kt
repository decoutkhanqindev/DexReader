package com.decoutkhanqindev.dexreader.domain.exception

sealed class FavoritesHistoryException(override val cause: Throwable? = null) :
  DomainException(cause) {
  data class PermissionDenied(override val cause: Throwable? = null) :
    FavoritesHistoryException(cause)
}

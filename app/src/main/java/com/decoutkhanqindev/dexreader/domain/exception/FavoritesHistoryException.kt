package com.decoutkhanqindev.dexreader.domain.exception

sealed class FavoritesHistoryException(override val cause: Throwable? = null) :
  DomainException(cause) {
  data class FavoriteNotFound(override val cause: Throwable? = null) :
    FavoritesHistoryException(cause)

  data class FavoriteAlreadyExists(override val cause: Throwable? = null) :
    FavoritesHistoryException(cause)

  data class HistoryNotFound(override val cause: Throwable? = null) :
    FavoritesHistoryException(cause)

  data class PermissionDenied(override val cause: Throwable? = null) :
    FavoritesHistoryException(cause)
}

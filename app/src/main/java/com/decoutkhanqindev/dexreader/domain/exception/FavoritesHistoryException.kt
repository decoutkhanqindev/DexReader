package com.decoutkhanqindev.dexreader.domain.exception

/** Sealed class for all favorites and history-related exceptions. */
sealed class FavoritesHistoryException(override val cause: Throwable? = null) :
  DomainException(cause) {
  /** Thrown when a favorite entry is not found. */
  data class FavoriteNotFound(override val cause: Throwable? = null) :
    FavoritesHistoryException(cause)

  /** Thrown when attempting to add a favorite that already exists. */
  data class FavoriteAlreadyExists(override val cause: Throwable? = null) :
    FavoritesHistoryException(cause)

  /** Thrown when a history entry is not found. */
  data class HistoryNotFound(override val cause: Throwable? = null) :
    FavoritesHistoryException(cause)
}

package com.decoutkhanqindev.dexreader.domain.exception

/** Sealed class for all favorites and history-related exceptions. */
sealed class FavoritesHistoryException(cause: Throwable? = null) : DomainException(cause) {
  /** Thrown when a favorite entry is not found. */
  class FavoriteNotFound(cause: Throwable? = null) : FavoritesHistoryException(cause)

  /** Thrown when attempting to add a favorite that already exists. */
  class FavoriteAlreadyExists(cause: Throwable? = null) : FavoritesHistoryException(cause)

  /** Thrown when a history entry is not found. */
  class HistoryNotFound(cause: Throwable? = null) : FavoritesHistoryException(cause)
}

package com.decoutkhanqindev.dexreader.domain.exception

/**
 * Sealed class for all favorites and history-related exceptions.
 */
sealed class FavoritesHistoryException(
  message: String,
  cause: Throwable? = null,
) : DomainException(message, cause) {
  /**
   * Thrown when a favorite entry is not found.
   */
  class FavoriteNotFound(message: String) : FavoritesHistoryException(message)

  /**
   * Thrown when attempting to add a favorite that already exists.
   */
  class FavoriteAlreadyExists(message: String) : FavoritesHistoryException(message)

  /**
   * Thrown when a history entry is not found.
   */
  class HistoryNotFound(message: String) : FavoritesHistoryException(message)
}

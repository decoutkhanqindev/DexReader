package com.decoutkhanqindev.dexreader.domain.exception

/**
 * Sealed class for all manga-related exceptions.
 */
sealed class MangaException(
  message: String,
  cause: Throwable? = null,
) : DomainException(message, cause) {
  /**
   * Thrown when a manga with the specified ID is not found.
   */
  class NotFound(message: String) : MangaException(message)

  /**
   * Thrown when a chapter with the specified ID is not found.
   */
  class ChapterNotFound(message: String) : MangaException(message)

  /**
   * Thrown when fetching manga data fails (network, parsing, or API errors).
   */
  class FetchFailed(
    message: String,
    cause: Throwable? = null,
  ) : MangaException(message, cause)
}

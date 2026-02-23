package com.decoutkhanqindev.dexreader.domain.exception

/** Sealed class for all manga-related exceptions. */
sealed class MangaException(cause: Throwable? = null) : DomainException(cause) {
  /** Thrown when a manga with the specified ID is not found. */
  class NotFound(cause: Throwable? = null) : MangaException(cause)

  /** Thrown when a chapter with the specified ID is not found. */
  class ChapterNotFound(cause: Throwable? = null) : MangaException(cause)

  /** Thrown when fetching manga data fails (network, parsing, or API errors). */
  class FetchFailed(cause: Throwable? = null) : MangaException(cause)
}

package com.decoutkhanqindev.dexreader.domain.exception

/** Sealed class for all manga-related exceptions. */
sealed class MangaException(override val cause: Throwable? = null) : DomainException(cause) {
  /** Thrown when a manga with the specified ID is not found. */
  data class NotFound(override val cause: Throwable? = null) : MangaException(cause)

  /** Thrown when a chapter with the specified ID is not found. */
  data class ChapterNotFound(override val cause: Throwable? = null) : MangaException(cause)

  /** Thrown when fetching manga data fails (network, parsing, or API errors). */
  data class FetchFailed(override val cause: Throwable? = null) : MangaException(cause)
}

package com.decoutkhanqindev.dexreader.domain.exception

sealed class MangaException(override val cause: Throwable? = null) : DomainException(cause) {
  data class NotFound(override val cause: Throwable? = null) : MangaException(cause)
  data class ChapterNotFound(override val cause: Throwable? = null) : MangaException(cause)
  data class FetchFailed(override val cause: Throwable? = null) : MangaException(cause)
}

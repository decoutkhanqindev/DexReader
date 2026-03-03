package com.decoutkhanqindev.dexreader.domain.exception

sealed class MangaException(cause: Throwable? = null) : DomainException(cause = cause) {
  class NotFound(cause: Throwable? = null) : MangaException(cause)
  class ChapterNotFound(cause: Throwable? = null) : MangaException(cause)
  class ChapterDataNotFound(cause: Throwable? = null) : MangaException(cause)
}

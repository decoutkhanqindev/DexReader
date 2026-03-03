package com.decoutkhanqindev.dexreader.domain.exception

sealed class CacheException(override val cause: Throwable? = null) : DomainException(cause) {
  data class NotFound(override val cause: Throwable? = null) : CacheException(cause)
}

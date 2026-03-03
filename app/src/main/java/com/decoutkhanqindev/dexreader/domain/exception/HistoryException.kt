package com.decoutkhanqindev.dexreader.domain.exception

sealed class HistoryException(cause: Throwable? = null) : DomainException(cause = cause) {
  class PermissionDenied(cause: Throwable? = null) : HistoryException(cause)
}

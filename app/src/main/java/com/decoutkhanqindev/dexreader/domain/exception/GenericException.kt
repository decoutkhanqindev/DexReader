package com.decoutkhanqindev.dexreader.domain.exception

/** Sealed class for generic infrastructure exceptions. */
sealed class GenericException(cause: Throwable? = null) : DomainException(cause) {
  /** Thrown when network operations fail (connectivity issues, timeouts, etc.). */
  class Network(cause: Throwable? = null) : GenericException(cause)

  /** Thrown when user lacks permission to perform an operation. */
  class PermissionDenied(cause: Throwable? = null) : GenericException(cause)

  /** Thrown when data validation fails or data is corrupted. */
  class InvalidData(cause: Throwable? = null) : GenericException(cause)

  /** Thrown when cache operations fail. */
  class Cache(cause: Throwable? = null) : GenericException(cause)
}

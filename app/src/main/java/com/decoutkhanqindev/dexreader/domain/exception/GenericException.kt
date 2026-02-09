package com.decoutkhanqindev.dexreader.domain.exception

/**
 * Sealed class for generic infrastructure exceptions.
 */
sealed class GenericException(
  message: String,
  cause: Throwable? = null,
) : DomainException(message, cause) {
  /**
   * Thrown when network operations fail (connectivity issues, timeouts, etc.).
   */
  class Network(
    message: String,
    cause: Throwable? = null,
  ) : GenericException(message, cause)

  /**
   * Thrown when user lacks permission to perform an operation.
   */
  class PermissionDenied(message: String) : GenericException(message)

  /**
   * Thrown when data validation fails or data is corrupted.
   */
  class InvalidData(message: String) : GenericException(message)

  /**
   * Thrown when cache operations fail.
   */
  class Cache(
    message: String,
    cause: Throwable? = null,
  ) : GenericException(message, cause)
}

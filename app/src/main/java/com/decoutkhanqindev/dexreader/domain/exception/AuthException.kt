package com.decoutkhanqindev.dexreader.domain.exception

/**
 * Sealed class for all authentication-related exceptions.
 */
sealed class AuthException(
  message: String,
  cause: Throwable? = null,
) : DomainException(message, cause) {
  /**
   * Thrown when email address is invalid or empty.
   */
  class InvalidEmail(message: String) : AuthException(message)

  /**
   * Thrown when password doesn't meet security requirements.
   */
  class WeakPassword(message: String) : AuthException(message)

  /**
   * Thrown when user account is not found during login or profile operations.
   */
  class UserNotFound(message: String) : AuthException(message)

  /**
   * Thrown when login credentials are incorrect.
   */
  class InvalidCredentials(message: String) : AuthException(message)

  /**
   * Thrown when attempting to register with an email that's already in use.
   */
  class UserAlreadyExists(message: String) : AuthException(message)
}

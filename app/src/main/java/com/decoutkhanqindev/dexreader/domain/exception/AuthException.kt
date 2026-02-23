package com.decoutkhanqindev.dexreader.domain.exception

/** Sealed class for all authentication-related exceptions. */
sealed class AuthException(cause: Throwable? = null) : DomainException(cause = cause) {
  /** Thrown when email address is invalid or empty. */
  class InvalidEmail(cause: Throwable? = null) : AuthException(cause)

  /** Thrown when password doesn't meet security requirements. */
  class WeakPassword(cause: Throwable? = null) : AuthException(cause)

  /** Thrown when user account is not found during login or profile operations. */
  class UserNotFound(cause: Throwable? = null) : AuthException(cause)

  /** Thrown when login credentials are incorrect. */
  class InvalidCredentials(cause: Throwable? = null) : AuthException(cause)

  /** Thrown when attempting to register with an email that's already in use. */
  class UserAlreadyExists(cause: Throwable? = null) : AuthException(cause)
}

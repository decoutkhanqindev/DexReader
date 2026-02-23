package com.decoutkhanqindev.dexreader.domain.exception

/** Sealed class for all authentication-related exceptions. */
sealed class AuthException(override val cause: Throwable? = null) : DomainException(cause = cause) {
  /** Thrown when email address is invalid or empty. */
  data class InvalidEmail(override val cause: Throwable? = null) : AuthException(cause)

  /** Thrown when password doesn't meet security requirements. */
  data class WeakPassword(override val cause: Throwable? = null) : AuthException(cause)

  /** Thrown when user account is not found during login or profile operations. */
  data class UserNotFound(override val cause: Throwable? = null) : AuthException(cause)

  /** Thrown when login credentials are incorrect. */
  data class InvalidCredentials(override val cause: Throwable? = null) : AuthException(cause)

  /** Thrown when attempting to register with an email that's already in use. */
  data class UserAlreadyExists(override val cause: Throwable? = null) : AuthException(cause)

  /** Thrown when user name is invalid or empty. */
  data class InvalidName(override val cause: Throwable? = null) : AuthException(cause)

  /** Thrown when password and confirm password do not match. */
  data class PasswordMismatch(override val cause: Throwable? = null) : AuthException(cause)
}

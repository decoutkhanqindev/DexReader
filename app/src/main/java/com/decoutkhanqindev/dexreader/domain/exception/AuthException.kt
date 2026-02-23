package com.decoutkhanqindev.dexreader.domain.exception

sealed class AuthException(override val cause: Throwable? = null) : DomainException(cause = cause) {
  sealed class Email(cause: Throwable? = null) : AuthException(cause) {
    data class Empty(override val cause: Throwable? = null) : Email(cause)
    data class Invalid(override val cause: Throwable? = null) : Email(cause)
  }

  sealed class Password(cause: Throwable? = null) : AuthException(cause) {
    data class Empty(override val cause: Throwable? = null) : Password(cause)
    data class Weak(override val cause: Throwable? = null) : Password(cause)
    data class Incorrect(override val cause: Throwable? = null) : Password(cause)
  }

  sealed class ConfirmPassword(cause: Throwable? = null) : AuthException(cause) {
    data class Empty(override val cause: Throwable? = null) : ConfirmPassword(cause)
    data class Mismatch(override val cause: Throwable? = null) : ConfirmPassword(cause)
  }

  sealed class Name(cause: Throwable? = null) : AuthException(cause) {
    data class Empty(override val cause: Throwable? = null) : Name(cause)
  }

  data class RegistrationFailed(override val cause: Throwable? = null) : AuthException(cause)
  data class UserNotFound(override val cause: Throwable? = null) : AuthException(cause)
  data class UserAlreadyExists(override val cause: Throwable? = null) : AuthException(cause)
}

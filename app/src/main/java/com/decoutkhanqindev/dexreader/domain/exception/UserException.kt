package com.decoutkhanqindev.dexreader.domain.exception

sealed class UserException(override val cause: Throwable? = null) : DomainException(cause = cause) {
  sealed class Email(cause: Throwable? = null) : UserException(cause) {
    data class Empty(override val cause: Throwable? = null) : Email(cause)
    data class Invalid(override val cause: Throwable? = null) : Email(cause)
  }

  sealed class Password(cause: Throwable? = null) : UserException(cause) {
    data class Empty(override val cause: Throwable? = null) : Password(cause)
    data class Weak(override val cause: Throwable? = null) : Password(cause)
    data class Incorrect(override val cause: Throwable? = null) : Password(cause)
  }

  sealed class ConfirmPassword(cause: Throwable? = null) : UserException(cause) {
    data class Empty(override val cause: Throwable? = null) : ConfirmPassword(cause)
    data class Mismatch(override val cause: Throwable? = null) : ConfirmPassword(cause)
  }

  sealed class Name(cause: Throwable? = null) : UserException(cause) {
    data class Empty(override val cause: Throwable? = null) : Name(cause)
  }

  data class RegistrationFailed(override val cause: Throwable? = null) : UserException(cause)
  data class NotFound(override val cause: Throwable? = null) : UserException(cause)
  data class AlreadyExists(override val cause: Throwable? = null) : UserException(cause)
}

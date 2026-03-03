package com.decoutkhanqindev.dexreader.domain.exception

sealed class UserException(cause: Throwable? = null) : DomainException(cause = cause) {
  sealed class Email(cause: Throwable? = null) : UserException(cause) {
    class Empty(cause: Throwable? = null) : Email(cause)
    class Invalid(cause: Throwable? = null) : Email(cause)
  }

  sealed class Password(cause: Throwable? = null) : UserException(cause) {
    class Empty(cause: Throwable? = null) : Password(cause)
    class Weak(cause: Throwable? = null) : Password(cause)
    class Incorrect(cause: Throwable? = null) : Password(cause)
  }

  sealed class ConfirmPassword(cause: Throwable? = null) : UserException(cause) {
    class Empty(cause: Throwable? = null) : ConfirmPassword(cause)
    class Mismatch(cause: Throwable? = null) : ConfirmPassword(cause)
  }

  sealed class Name(cause: Throwable? = null) : UserException(cause) {
    class Empty(cause: Throwable? = null) : Name(cause)
  }

  class RegistrationFailed(cause: Throwable? = null) : UserException(cause)
  class NotFound(cause: Throwable? = null) : UserException(cause)
  class AlreadyExists(cause: Throwable? = null) : UserException(cause)
}

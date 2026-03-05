package com.decoutkhanqindev.dexreader.domain.exception

sealed class ValidationException(
  message: String? = null,
  cause: Throwable? = null,
) : DomainException(message, cause) {

  sealed class Email(message: String) : ValidationException(message) {
    class Empty : Email("Email must not be empty")
    class Invalid : Email("Email format is invalid")
  }

  sealed class Password(message: String) : ValidationException(message) {
    class Empty : Password("Password must not be empty")
    class TooWeak : Password("Password must be at least 8 characters")
  }

  sealed class ConfirmPassword(message: String) : ValidationException(message) {
    class Empty : ConfirmPassword("Confirm password must not be empty")
    class Mismatch : ConfirmPassword("Passwords do not match")
  }

  sealed class Name(message: String) : ValidationException(message) {
    class Empty : Name("Name must not be empty")
  }
}

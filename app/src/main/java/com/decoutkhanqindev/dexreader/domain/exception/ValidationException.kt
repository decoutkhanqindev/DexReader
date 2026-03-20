package com.decoutkhanqindev.dexreader.domain.exception

sealed class ValidationException(
  message: String? = null,
  cause: Throwable? = null,
) : DomainException(message, cause) {

  sealed class Email(message: String) : ValidationException(message) {
    data object Empty : Email("Email must not be empty")
    data object Invalid : Email("Email format is invalid")
  }

  sealed class Password(message: String) : ValidationException(message) {
    data object Empty : Password("Password must not be empty")
    data object TooWeak : Password("Password must be at least 8 characters")
  }

  sealed class ConfirmPassword(message: String) : ValidationException(message) {
    data object Empty : ConfirmPassword("Confirm password must not be empty")
    data object Mismatch : ConfirmPassword("Passwords do not match")
  }

  sealed class Name(message: String) : ValidationException(message) {
    data object Empty : Name("Name must not be empty")
  }
}

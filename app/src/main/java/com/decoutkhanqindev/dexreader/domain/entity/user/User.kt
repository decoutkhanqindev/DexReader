package com.decoutkhanqindev.dexreader.domain.entity.user

import com.decoutkhanqindev.dexreader.domain.exception.ValidationException

data class User(
  val id: String,
  val name: String,
  val email: String,
  val avatarUrl: String?,
) {
  companion object {
    const val DEFAULT_NAME = ""
    const val DEFAULT_EMAIL = ""
    private const val MIN_PASSWORD_LENGTH = 8
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()

    fun validateEmail(email: String) {
      if (email.isBlank()) throw ValidationException.Email.Empty
      if (!EMAIL_REGEX.matches(email)) throw ValidationException.Email.Invalid
    }

    fun validatePassword(password: String) {
      if (password.isBlank()) throw ValidationException.Password.Empty
      if (password.length < MIN_PASSWORD_LENGTH) throw ValidationException.Password.TooWeak
    }

    fun validateConfirmPassword(password: String, confirmPassword: String) {
      if (confirmPassword.isBlank()) throw ValidationException.ConfirmPassword.Empty
      if (password != confirmPassword) throw ValidationException.ConfirmPassword.Mismatch
    }

    fun validateName(name: String) {
      if (name.isBlank()) throw ValidationException.Name.Empty
    }
  }
}

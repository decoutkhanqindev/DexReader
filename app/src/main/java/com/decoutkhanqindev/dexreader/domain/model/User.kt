package com.decoutkhanqindev.dexreader.domain.model

import com.decoutkhanqindev.dexreader.domain.exception.UserException

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
      if (email.isBlank()) throw UserException.Email.Empty()
      if (!EMAIL_REGEX.matches(email)) throw UserException.Email.Invalid()
    }

    fun validatePassword(password: String) {
      if (password.isBlank()) throw UserException.Password.Empty()
      if (password.length < MIN_PASSWORD_LENGTH) throw UserException.Password.Weak()
    }

    fun validateConfirmPassword(password: String, confirmPassword: String) {
      if (confirmPassword.isBlank()) throw UserException.ConfirmPassword.Empty()
      if (password != confirmPassword) throw UserException.ConfirmPassword.Mismatch()
    }

    fun validateName(name: String) {
      if (name.isBlank()) throw UserException.Name.Empty()
    }
  }
}

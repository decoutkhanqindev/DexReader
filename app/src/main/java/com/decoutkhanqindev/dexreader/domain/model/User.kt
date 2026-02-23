package com.decoutkhanqindev.dexreader.domain.model

import com.decoutkhanqindev.dexreader.domain.exception.AuthException

data class User(
  val id: String,
  val name: String,
  val email: String,
  val profilePictureUrl: String?,
) {
  companion object {
    private const val MIN_PASSWORD_LENGTH = 8
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()

    fun validateEmail(email: String) {
      if (email.isBlank()) throw AuthException.Email.Empty()
      if (!EMAIL_REGEX.matches(email)) throw AuthException.Email.Invalid()
    }

    fun validatePassword(password: String) {
      if (password.isBlank()) throw AuthException.Password.Empty()
      if (password.length < MIN_PASSWORD_LENGTH) throw AuthException.Password.Weak()
    }

    fun validateConfirmPassword(password: String, confirmPassword: String) {
      if (confirmPassword.isBlank()) throw AuthException.ConfirmPassword.Empty()
      if (password != confirmPassword) throw AuthException.ConfirmPassword.Mismatch()
    }

    fun validateName(name: String) {
      if (name.isBlank()) throw AuthException.Name.Empty()
    }
  }
}

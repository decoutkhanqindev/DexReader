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
      if (email.isBlank() || !EMAIL_REGEX.matches(email)) throw AuthException.InvalidEmail()
    }

    fun validatePassword(password: String) {
      if (password.length < MIN_PASSWORD_LENGTH) throw AuthException.WeakPassword()
    }

    fun validateConfirmPassword(password: String, confirmPassword: String) {
      if (password != confirmPassword) throw AuthException.PasswordMismatch()
    }

    fun validateName(name: String) {
      if (name.isBlank()) throw AuthException.InvalidName()
    }
  }
}

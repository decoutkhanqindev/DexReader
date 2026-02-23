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

    fun validate(email: String, password: String? = null) {
      if (email.isBlank() || !EMAIL_REGEX.matches(email)) throw AuthException.InvalidEmail()
      if (password != null && password.length < MIN_PASSWORD_LENGTH) throw AuthException.WeakPassword()
    }
  }
}

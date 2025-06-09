package com.decoutkhanqindev.dexreader.presentation.ui.auth.login

import com.decoutkhanqindev.dexreader.presentation.ui.auth.AuthError

data class LoginUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val password: String = "",
  val isValidEmail: Boolean = false,
  val isValidPassword: Boolean = false,
  val isSuccess: Boolean = false,
  val emailError: AuthError = AuthError.EmailError.Required,
  val passwordError: AuthError = AuthError.PasswordError.Required,
  val userError: AuthError = AuthError.UnknownError,
  val isError: Boolean = false
)
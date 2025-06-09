package com.decoutkhanqindev.dexreader.presentation.ui.auth.register

import com.decoutkhanqindev.dexreader.presentation.ui.auth.AuthError

data class RegisterUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val password: String = "",
  val confirmPassword: String = "",
  val name: String = "",
  val isValidEmail: Boolean = false,
  val isValidPassword: Boolean = false,
  val isValidConfirmPassword: Boolean = false,
  val isValidName: Boolean = false,
  val isSuccess: Boolean = false,
  val emailError: AuthError = AuthError.UnknownError,
  val passwordError: AuthError = AuthError.UnknownError,
  val confirmPasswordError: AuthError = AuthError.UnknownError,
  val nameError: AuthError = AuthError.UnknownError,
)

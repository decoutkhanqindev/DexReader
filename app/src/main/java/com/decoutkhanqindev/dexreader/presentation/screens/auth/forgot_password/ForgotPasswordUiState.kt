package com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password

import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthError

data class ForgotPasswordUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val isValidEmail: Boolean = false,
  val isSuccess: Boolean = false,
  val emailError: AuthError = AuthError.EmailError.Required,
  val isError: Boolean = false
)
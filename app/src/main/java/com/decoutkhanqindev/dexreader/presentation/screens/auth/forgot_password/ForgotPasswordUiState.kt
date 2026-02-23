package com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthError

@Immutable
data class ForgotPasswordUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val isSuccess: Boolean = false,
  val emailError: AuthError? = null,
  val isError: Boolean = false,
)

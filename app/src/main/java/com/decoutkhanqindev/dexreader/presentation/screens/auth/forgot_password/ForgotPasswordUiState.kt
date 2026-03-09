package com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.error.UserUiError

@Immutable
data class ForgotPasswordUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val isSuccess: Boolean = false,
  val emailError: UserUiError? = null,
  val isError: Boolean = false,
)

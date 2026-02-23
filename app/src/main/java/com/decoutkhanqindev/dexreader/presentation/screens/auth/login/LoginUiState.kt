package com.decoutkhanqindev.dexreader.presentation.screens.auth.login

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthError

@Immutable
data class LoginUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val password: String = "",
  val isSuccess: Boolean = false,
  val emailError: AuthError? = null,
  val passwordError: AuthError? = null,
  val userError: AuthError? = null,
  val isError: Boolean = false,
)

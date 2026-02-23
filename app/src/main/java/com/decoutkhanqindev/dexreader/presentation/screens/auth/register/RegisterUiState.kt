package com.decoutkhanqindev.dexreader.presentation.screens.auth.register

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthError

@Immutable
data class RegisterUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val password: String = "",
  val confirmPassword: String = "",
  val name: String = "",
  val isSuccess: Boolean = false,
  val emailError: AuthError? = null,
  val passwordError: AuthError? = null,
  val confirmPasswordError: AuthError? = null,
  val nameError: AuthError? = null,
  val isError: Boolean = false,
)

package com.decoutkhanqindev.dexreader.presentation.screens.auth.register

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.error.UserUiError

@Immutable
data class RegisterUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val password: String = "",
  val confirmPassword: String = "",
  val name: String = "",
  val isSuccess: Boolean = false,
  val emailError: UserUiError? = null,
  val passwordError: UserUiError? = null,
  val confirmPasswordError: UserUiError? = null,
  val nameError: UserUiError? = null,
  val isError: Boolean = false,
)

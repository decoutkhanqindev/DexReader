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
  val isValidEmail: Boolean = false,
  val isValidPassword: Boolean = false,
  val isValidConfirmPassword: Boolean = false,
  val isValidName: Boolean = false,
  val isSuccess: Boolean = false,
  val emailError: AuthError = AuthError.EmailError.Required,
  val passwordError: AuthError = AuthError.PasswordError.Required,
  val confirmPasswordError: AuthError = AuthError.ConfirmPasswordError.Required,
  val nameError: AuthError = AuthError.NameError.Required,
  val isError: Boolean = false,
)

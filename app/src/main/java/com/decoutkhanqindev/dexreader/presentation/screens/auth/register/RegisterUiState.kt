package com.decoutkhanqindev.dexreader.presentation.screens.auth.register


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.error.UserError

@Immutable
data class RegisterUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val password: String = "",
  val confirmPassword: String = "",
  val name: String = "",
  val isSuccess: Boolean = false,
  val emailError: UserError? = null,
  val passwordError: UserError? = null,
  val confirmPasswordError: UserError? = null,
  val nameError: UserError? = null,
  val isError: Boolean = false,
)

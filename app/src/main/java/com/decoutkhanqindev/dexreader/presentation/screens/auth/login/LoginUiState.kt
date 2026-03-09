package com.decoutkhanqindev.dexreader.presentation.screens.auth.login


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.error.UserUiError

@Immutable
data class LoginUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val password: String = "",
  val isSuccess: Boolean = false,
  val emailError: UserUiError? = null,
  val passwordError: UserUiError? = null,
  val userError: UserUiError? = null,
  val isError: Boolean = false,
)

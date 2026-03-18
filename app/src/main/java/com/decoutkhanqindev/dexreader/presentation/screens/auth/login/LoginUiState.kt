package com.decoutkhanqindev.dexreader.presentation.screens.auth.login


import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.presentation.model.error.UserError

@Immutable
data class LoginUiState(
  val isLoading: Boolean = false,
  val email: String = "",
  val password: String = "",
  val isSuccess: Boolean = false,
  val emailError: UserError? = null,
  val passwordError: UserError? = null,
  val userError: UserError? = null,
  val isError: Boolean = false,
)

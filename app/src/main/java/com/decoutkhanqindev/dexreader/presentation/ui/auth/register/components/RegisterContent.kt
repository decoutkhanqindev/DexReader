package com.decoutkhanqindev.dexreader.presentation.ui.auth.register.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.ui.auth.AuthContent
import com.decoutkhanqindev.dexreader.presentation.ui.auth.register.RegisterUiState

@Composable
fun RegisterContent(
  uiState: RegisterUiState,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onConfirmPasswordChange: (String) -> Unit,
  onNameChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onRegisterSuccess: (
    email: String,
    password: String,
  ) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  AuthContent(
    modifier = modifier,
    content = {
      if (uiState.isSuccess) {
        onRegisterSuccess(uiState.email, uiState.password)
      }

      RegisterForm(
        uiState = uiState,
        onEmailChange = onEmailChange,
        onPasswordChange = onPasswordChange,
        onConfirmPasswordChange = onConfirmPasswordChange,
        onNameChange = onNameChange,
        onSubmitClick = onSubmitClick,
        onNavigateBack = onNavigateBack,
        modifier = Modifier.fillMaxSize()
      )
    }
  )
}
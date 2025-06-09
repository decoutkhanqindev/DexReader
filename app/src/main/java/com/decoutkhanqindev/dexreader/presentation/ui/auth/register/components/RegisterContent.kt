package com.decoutkhanqindev.dexreader.presentation.ui.auth.register.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.ui.auth.AuthContent
import com.decoutkhanqindev.dexreader.presentation.ui.auth.register.RegisterUiState
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen

@Composable
fun RegisterContent(
  uiState: RegisterUiState,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onConfirmPasswordChange: (String) -> Unit,
  onNameChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onRegisterSuccess: () -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  AuthContent(
    modifier = modifier,
    content = {
      when {
        uiState.isLoading == true -> LoadingScreen(modifier = Modifier.fillMaxSize())
        uiState.isError == true -> {}
        uiState.isSuccess == true -> onRegisterSuccess()
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
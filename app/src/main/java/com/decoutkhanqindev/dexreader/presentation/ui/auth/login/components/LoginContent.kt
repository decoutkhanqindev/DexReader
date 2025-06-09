package com.decoutkhanqindev.dexreader.presentation.ui.auth.login.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.ui.auth.AuthContent
import com.decoutkhanqindev.dexreader.presentation.ui.auth.login.LoginUiState
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen

@Composable
fun LoginContent(
  uiState: LoginUiState,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onLoginSuccess: () -> Unit,
  onRegisterClick: () -> Unit,
  onForgotPasswordClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  AuthContent(
    modifier = modifier,
    content = {
      when {
        uiState.isLoading == true -> LoadingScreen(modifier = Modifier.fillMaxSize())
        uiState.isError == true -> {}
        uiState.isSuccess == true -> onLoginSuccess()
      }

      LoginForm(
        uiState = uiState,
        onEmailChange = onEmailChange,
        onPasswordChange = onPasswordChange,
        onSubmitClick = onSubmitClick,
        onRegisterClick = onRegisterClick,
        onForgotPasswordClick = onForgotPasswordClick,
        modifier = Modifier.fillMaxSize()
      )
    }
  )
}
package com.decoutkhanqindev.dexreader.presentation.screens.auth.login.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthContent
import com.decoutkhanqindev.dexreader.presentation.screens.auth.login.LoginUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.blurBackground
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.AlertDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun LoginContent(
  uiState: LoginUiState,
  modifier: Modifier = Modifier,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onLoginSuccess: () -> Unit,
  onRegisterClick: () -> Unit,
  onForgotPasswordClick: () -> Unit,
  onRetry: () -> Unit,
) {
  var isShowErrorDialog by remember(uiState.isError) { mutableStateOf(uiState.isError) }
  var isShowSuccessDialog by remember(uiState.isSuccess) { mutableStateOf(uiState.isSuccess) }

  Box(modifier = modifier) {
    AuthContent(
      modifier = Modifier
        .fillMaxSize()
        .then(
          if (uiState.isLoading) {
            Modifier.blurBackground(
              topAlpha = 0.7f,
              bottomAlpha = 0.7f,
            )
          } else Modifier
        )
    ) {
      LoginForm(
        email = uiState.email,
        emailError = uiState.emailError,
        password = uiState.password,
        passwordError = uiState.passwordError,
        modifier = Modifier.fillMaxSize(),
        onEmailChange = onEmailChange,
        onPasswordChange = onPasswordChange,
        onSubmitClick = onSubmitClick,
        onRegisterClick = onRegisterClick,
        onForgotPasswordClick = onForgotPasswordClick,
      )
    }

    when {
      uiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      uiState.isError -> {
        if (isShowErrorDialog) {
          AlertDialog(
            title = stringResource(R.string.sign_in_failed_please_try_again),
            onConfirmClick = onRetry,
            onDismissClick = { isShowErrorDialog = false },
          )
        }
      }

      uiState.isSuccess -> {
        if (isShowSuccessDialog) {
          AlertDialog(
            icon = Icons.Default.Done,
            title = stringResource(R.string.sign_in_successful),
            confirm = stringResource(R.string.ok),
            isEnableDismiss = false,
            onConfirmClick = {
              isShowSuccessDialog = false
              onLoginSuccess()
            },
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun LoginContentPreview() {
  DexReaderTheme {
    LoginContent(
      uiState = LoginUiState(),
      modifier = Modifier.fillMaxSize(),
      onEmailChange = {},
      onPasswordChange = {},
      onSubmitClick = {},
      onLoginSuccess = {},
      onRegisterClick = {},
      onForgotPasswordClick = {},
      onRetry = {})
  }
}

@Preview
@Composable
private fun LoginContentLoadingPreview() {
  DexReaderTheme {
    LoginContent(
      uiState = LoginUiState(isLoading = true),
      modifier = Modifier.fillMaxSize(),
      onEmailChange = {},
      onPasswordChange = {},
      onSubmitClick = {},
      onLoginSuccess = {},
      onRegisterClick = {},
      onForgotPasswordClick = {},
      onRetry = {})
  }
}

@Preview
@Composable
private fun LoginContentErrorPreview() {
  DexReaderTheme {
    LoginContent(
      uiState = LoginUiState(isError = true),
      modifier = Modifier.fillMaxSize(),
      onEmailChange = {},
      onPasswordChange = {},
      onSubmitClick = {},
      onLoginSuccess = {},
      onRegisterClick = {},
      onForgotPasswordClick = {},
      onRetry = {})
  }
}

@Preview
@Composable
private fun LoginContentSuccessPreview() {
  DexReaderTheme {
    LoginContent(
      uiState = LoginUiState(isSuccess = true),
      modifier = Modifier.fillMaxSize(),
      onEmailChange = {},
      onPasswordChange = {},
      onSubmitClick = {},
      onLoginSuccess = {},
      onRegisterClick = {},
      onForgotPasswordClick = {},
      onRetry = {})
  }
}

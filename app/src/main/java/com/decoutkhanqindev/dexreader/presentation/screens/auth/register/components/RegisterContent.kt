package com.decoutkhanqindev.dexreader.presentation.screens.auth.register.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthContent
import com.decoutkhanqindev.dexreader.presentation.screens.auth.register.RegisterUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun RegisterContent(
  uiState: RegisterUiState,
  modifier: Modifier = Modifier,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onConfirmPasswordChange: (String) -> Unit,
  onNameChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onRegisterSuccess: () -> Unit,
  onNavigateBack: () -> Unit,
  onRetry: () -> Unit,
) {
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }
  var isShowSuccessDialog by rememberSaveable { mutableStateOf(true) }

  Box(
    modifier =
      if (uiState.isLoading) modifier.blur(8.dp)
      else modifier
  ) {
    AuthContent(modifier = Modifier.fillMaxSize()) {
      RegisterForm(
        email = uiState.email,
        emailError = uiState.emailError,
        password = uiState.password,
        passwordError = uiState.passwordError,
        confirmPassword = uiState.confirmPassword,
        confirmPasswordError = uiState.confirmPasswordError,
        name = uiState.name,
        nameError = uiState.nameError,
        modifier = Modifier.fillMaxSize(),
        onEmailChange = onEmailChange,
        onPasswordChange = onPasswordChange,
        onConfirmPasswordChange = onConfirmPasswordChange,
        onNameChange = onNameChange,
        onSubmitClick = onSubmitClick,
        onNavigateBack = onNavigateBack,
      )
    }

    when {
      uiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      uiState.isError -> {
        if (isShowErrorDialog) {
          NotificationDialog(
            title = stringResource(R.string.sign_up_failed_please_try_again),
            onConfirmClick = onRetry,
            onDismissClick = { isShowErrorDialog = false },
          )
        }
      }

      uiState.isSuccess -> {
        if (isShowSuccessDialog) {
          NotificationDialog(
            icon = Icons.Default.Done,
            title = stringResource(R.string.sign_up_successful),
            confirm = stringResource(R.string.ok),
            isEnableDismiss = false,
            onConfirmClick = {
              isShowSuccessDialog = false
              onRegisterSuccess()
            },
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun RegisterContentPreview() {
  DexReaderTheme {
    RegisterContent(
      uiState = RegisterUiState(),
      modifier = Modifier.fillMaxSize(),
      onEmailChange = {},
      onPasswordChange = {},
      onConfirmPasswordChange = {},
      onNameChange = {},
      onSubmitClick = {},
      onRegisterSuccess = {},
      onNavigateBack = {},
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun RegisterContentLoadingPreview() {
  DexReaderTheme {
    RegisterContent(
      uiState = RegisterUiState(isLoading = true),
      modifier = Modifier.fillMaxSize(),
      onEmailChange = {},
      onPasswordChange = {},
      onConfirmPasswordChange = {},
      onNameChange = {},
      onSubmitClick = {},
      onRegisterSuccess = {},
      onNavigateBack = {},
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun RegisterContentErrorPreview() {
  DexReaderTheme {
    RegisterContent(
      uiState = RegisterUiState(isError = true),
      modifier = Modifier.fillMaxSize(),
      onEmailChange = {},
      onPasswordChange = {},
      onConfirmPasswordChange = {},
      onNameChange = {},
      onSubmitClick = {},
      onRegisterSuccess = {},
      onNavigateBack = {},
      onRetry = {}
    )
  }
}

@Preview
@Composable
private fun RegisterContentSuccessPreview() {
  DexReaderTheme {
    RegisterContent(
      uiState = RegisterUiState(isSuccess = true),
      modifier = Modifier.fillMaxSize(),
      onEmailChange = {},
      onPasswordChange = {},
      onConfirmPasswordChange = {},
      onNameChange = {},
      onSubmitClick = {},
      onRegisterSuccess = {},
      onNavigateBack = {},
      onRetry = {}
    )
  }
}

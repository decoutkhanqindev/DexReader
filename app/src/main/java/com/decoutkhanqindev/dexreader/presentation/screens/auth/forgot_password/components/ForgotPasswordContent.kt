package com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.components

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
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthContent
import com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.ForgotPasswordUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen

@Composable
fun ForgotPasswordContent(
  uiState: ForgotPasswordUiState,
  modifier: Modifier = Modifier,
  onEmailChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onSubmitSuccess: () -> Unit,
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
      ForgotPasswordForm(
        email = uiState.email,
        emailError = uiState.emailError,
        modifier = Modifier.fillMaxSize(),
        onEmailChange = onEmailChange,
        onSubmitClick = onSubmitClick,
        onNavigateBack = onNavigateBack,
      )
    }

    when {
      uiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      uiState.isError -> {
        if (isShowErrorDialog) {
          NotificationDialog(
            title = stringResource(R.string.submit_reset_password_failed_please_try_again),
            onConfirmClick = onRetry,
            onDismissClick = { isShowErrorDialog = false },
          )
        }
      }

      uiState.isSuccess -> {
        if (isShowSuccessDialog) {
          NotificationDialog(
            icon = Icons.Default.Done,
            title = stringResource(R.string.submit_reset_password_successful),
            confirm = stringResource(R.string.ok),
            isEnableDismiss = false,
            onConfirmClick = {
              isShowSuccessDialog = false
              onSubmitSuccess()
            },
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun ForgotPasswordContentPreview() {
  ForgotPasswordContent(
    uiState = ForgotPasswordUiState(),
    modifier = Modifier.fillMaxSize(),
    onEmailChange = {},
    onSubmitClick = {},
    onSubmitSuccess = {},
    onNavigateBack = {},
    onRetry = {}
  )
}

@Preview
@Composable
private fun ForgotPasswordContentLoadingPreview() {
  ForgotPasswordContent(
    uiState = ForgotPasswordUiState(isLoading = true),
    modifier = Modifier.fillMaxSize(),
    onEmailChange = {},
    onSubmitClick = {},
    onSubmitSuccess = {},
    onNavigateBack = {},
    onRetry = {}
  )
}

@Preview
@Composable
private fun ForgotPasswordContentErrorPreview() {
  ForgotPasswordContent(
    uiState = ForgotPasswordUiState(isError = true),
    modifier = Modifier.fillMaxSize(),
    onEmailChange = {},
    onSubmitClick = {},
    onSubmitSuccess = {},
    onNavigateBack = {},
    onRetry = {}
  )
}

@Preview
@Composable
private fun ForgotPasswordContentSuccessPreview() {
  ForgotPasswordContent(
    uiState = ForgotPasswordUiState(isSuccess = true),
    modifier = Modifier.fillMaxSize(),
    onEmailChange = {},
    onSubmitClick = {},
    onSubmitSuccess = {},
    onNavigateBack = {},
    onRetry = {}
  )
}

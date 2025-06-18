package com.decoutkhanqindev.dexreader.presentation.screens.auth.login.components

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
import com.decoutkhanqindev.dexreader.presentation.screens.auth.login.LoginUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen

@Composable
fun LoginContent(
  uiState: LoginUiState,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onLoginSuccess: () -> Unit,
  onRegisterClick: () -> Unit,
  onForgotPasswordClick: () -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isShowSuccessDialog by rememberSaveable { mutableStateOf(true) }
  var isShowErrorDialog by rememberSaveable { mutableStateOf(true) }

  Box(
    modifier =
      if (uiState.isLoading) modifier.blur(8.dp)
      else modifier
  ) {
    AuthContent(
      modifier = Modifier.fillMaxSize(),
      content = {
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

  when {
    uiState.isLoading -> LoadingScreen(modifier = modifier)

    uiState.isError -> {
      if (isShowErrorDialog) {
        NotificationDialog(
          title = stringResource(R.string.sign_in_failed_please_try_again),
          onDismissClick = { isShowErrorDialog = false },
          onConfirmClick = onRetry,
        )
      }
    }

    uiState.isSuccess -> {
      if (isShowSuccessDialog) {
        NotificationDialog(
          icon = Icons.Default.Done,
          title = stringResource(R.string.sign_in_successful),
          isEnableDismiss = false,
          confirm = stringResource(R.string.ok),
          onConfirmClick = {
            isShowSuccessDialog = false
            onLoginSuccess()
          },
        )
      }
    }
  }
}
package com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.presentation.screens.auth.AuthContent
import com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.ForgotPasswordUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen

@Composable
fun ForgotPasswordContent(
  uiState: ForgotPasswordUiState,
  onEmailChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onSubmitSuccess: () -> Unit,
  onNavigateBack: () -> Unit,
  onRetry: () -> Unit,
  onDismissError: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val contentModifier = remember(uiState.isLoading, modifier) {
    if (uiState.isLoading) modifier.blur(8.dp) else modifier
  }

  Box(modifier = contentModifier) {
    AuthContent(
      modifier = Modifier.fillMaxSize(),
      content = {
        ForgotPasswordForm(
          email = uiState.email,
          emailError = uiState.emailError,
          onEmailChange = onEmailChange,
          onSubmitClick = onSubmitClick,
          onNavigateBack = onNavigateBack,
          modifier = Modifier.fillMaxSize()
        )
      }
    )

    when {
      uiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      uiState.isError -> {
        NotificationDialog(
          onConfirmClick = onRetry,
          title = stringResource(R.string.submit_reset_password_failed_please_try_again),
          onDismissClick = onDismissError,
        )
      }

      uiState.isSuccess -> {
        NotificationDialog(
          onConfirmClick = onSubmitSuccess,
          icon = Icons.Default.Done,
          title = stringResource(R.string.submit_reset_password_successful),
          isEnableDismiss = false,
          confirm = stringResource(R.string.ok),
        )
      }
    }
  }
}

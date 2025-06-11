package com.decoutkhanqindev.dexreader.presentation.ui.auth.forgot_password.components

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
import com.decoutkhanqindev.dexreader.presentation.ui.auth.AuthContent
import com.decoutkhanqindev.dexreader.presentation.ui.auth.forgot_password.ForgotPasswordUiState
import com.decoutkhanqindev.dexreader.presentation.ui.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen

@Composable
fun ForgotPasswordContent(
  uiState: ForgotPasswordUiState,
  onEmailChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onSubmitSuccess: () -> Unit,
  onNavigateBack: () -> Unit,
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
        ForgotPasswordForm(
          uiState = uiState,
          onEmailChange = onEmailChange,
          onSubmitClick = onSubmitClick,
          onNavigateBack = onNavigateBack,
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
          title = stringResource(R.string.submit_reset_password_failed_please_try_again),
          onDismissClick = { isShowErrorDialog = false },
          onConfirmClick = onRetry,
        )
      }
    }

    uiState.isSuccess -> {
      if (isShowSuccessDialog) {
        NotificationDialog(
          icon = Icons.Default.Done,
          title = stringResource(R.string.submit_reset_password_successful),
          isEnableDismiss = false,
          confirm = stringResource(R.string.ok),
          onConfirmClick = {
            isShowSuccessDialog = false
            onSubmitSuccess()
          },
        )
      }
    }
  }
}
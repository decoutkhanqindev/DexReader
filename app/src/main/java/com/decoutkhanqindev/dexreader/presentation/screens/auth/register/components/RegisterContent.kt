package com.decoutkhanqindev.dexreader.presentation.screens.auth.register.components

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
import com.decoutkhanqindev.dexreader.presentation.screens.auth.register.RegisterUiState
import com.decoutkhanqindev.dexreader.presentation.screens.common.dialog.NotificationDialog
import com.decoutkhanqindev.dexreader.presentation.screens.common.states.LoadingScreen

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
  onDismissError: () -> Unit,
) {
  val contentModifier = remember(uiState.isLoading, modifier) {
    if (uiState.isLoading) modifier.blur(8.dp) else modifier
  }

  Box(modifier = contentModifier) {
    AuthContent(
      modifier = Modifier.fillMaxSize(),
      content = {
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
    )

    when {
      uiState.isLoading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      uiState.isError -> {
        NotificationDialog(
          onConfirmClick = onRetry,
          title = stringResource(R.string.sign_up_failed_please_try_again),
          onDismissClick = onDismissError,
        )
      }

      uiState.isSuccess -> {
        NotificationDialog(
          onConfirmClick = onRegisterSuccess,
          icon = Icons.Default.Done,
          title = stringResource(R.string.sign_up_successful),
          isEnableDismiss = false,
          confirm = stringResource(R.string.ok),
        )
      }
    }
  }
}

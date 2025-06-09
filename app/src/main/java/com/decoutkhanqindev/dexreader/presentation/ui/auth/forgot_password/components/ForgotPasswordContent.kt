package com.decoutkhanqindev.dexreader.presentation.ui.auth.forgot_password.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.ui.auth.AuthContent
import com.decoutkhanqindev.dexreader.presentation.ui.auth.forgot_password.ForgotPasswordUiState
import com.decoutkhanqindev.dexreader.presentation.ui.common.states.LoadingScreen

@Composable
fun ForgotPasswordContent(
  uiState: ForgotPasswordUiState,
  onEmailChange: (String) -> Unit,
  onSubmitClick: () -> Unit,
  onSubmitSuccess: () -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  AuthContent(
    modifier = modifier,
    content = {
      when {
        uiState.isLoading == true -> LoadingScreen(modifier = Modifier.fillMaxSize())
        uiState.isError == true -> {}
        uiState.isSuccess == true -> onSubmitSuccess()
      }

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
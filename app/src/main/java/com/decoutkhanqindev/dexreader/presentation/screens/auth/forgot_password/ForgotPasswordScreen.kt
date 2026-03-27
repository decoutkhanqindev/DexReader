package com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.screens.auth.forgot_password.components.ForgotPasswordContent

@Composable
fun ForgotPasswordScreen(
  onNavigateBack: () -> Unit,
  onNavigateToLoginScreen: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: ForgotPasswordViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  ForgotPasswordContent(
    uiState = uiState,
    onEmailChange = viewModel::updateEmail,
    onSubmitClick = viewModel::submit,
    onSubmitSuccess = onNavigateToLoginScreen,
    onNavigateBack = onNavigateBack,
    onRetry = viewModel::retry,
    onDismissError = viewModel::dismissError,
    modifier = modifier
  )
}
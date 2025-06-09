package com.decoutkhanqindev.dexreader.presentation.ui.auth.forgot_password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.ui.auth.forgot_password.components.ForgotPasswordContent

@Composable
fun ForgotPasswordScreen(
  onNavigateBack: () -> Unit,
  onSubmitSuccess: () -> Unit,
  viewModel: ForgotPasswordViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  ForgotPasswordContent(
    uiState = uiState,
    onEmailChange = { viewModel.updateEmailField(email = it) },
    onSubmitClick = { viewModel.sendResetUserPassword() },
    onSubmitSuccess = onSubmitSuccess,
    onNavigateBack = onNavigateBack,
    modifier = modifier
  )
}
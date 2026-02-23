package com.decoutkhanqindev.dexreader.presentation.screens.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.screens.auth.login.components.LoginContent

@Composable
fun LoginScreen(
  onLoginSuccess: () -> Unit,
  onRegisterClick: () -> Unit,
  onForgotPasswordClick: () -> Unit,
  viewModel: LoginViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LoginContent(
    uiState = uiState,
    onEmailChange = viewModel::updateEmail,
    onPasswordChange = viewModel::updatePassword,
    onSubmitClick = viewModel::submit,
    onLoginSuccess = onLoginSuccess,
    onRegisterClick = onRegisterClick,
    onForgotPasswordClick = onForgotPasswordClick,
    onRetry = viewModel::retry,
    modifier = modifier
  )
}
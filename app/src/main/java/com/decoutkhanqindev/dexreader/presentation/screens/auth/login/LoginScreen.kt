package com.decoutkhanqindev.dexreader.presentation.screens.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.screens.auth.login.components.LoginContent

@Composable
fun LoginScreen(
  onNavigateToHomeScreen: () -> Unit,
  onNavigateToRegisterScreen: () -> Unit,
  onNavigateToForgotPasswordScreen: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: LoginViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LoginContent(
    uiState = uiState,
    onEmailChange = viewModel::updateEmail,
    onPasswordChange = viewModel::updatePassword,
    onSubmitClick = viewModel::submit,
    onLoginSuccess = onNavigateToHomeScreen,
    onRegisterClick = onNavigateToRegisterScreen,
    onForgotPasswordClick = onNavigateToForgotPasswordScreen,
    onRetry = viewModel::retry,
    onDismissError = viewModel::dismissError,
    modifier = modifier
  )
}
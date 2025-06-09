package com.decoutkhanqindev.dexreader.presentation.ui.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.ui.auth.login.components.LoginContent

@Composable
fun LoginScreen(
  onLoginSuccess: () -> Unit,
  onRegisterClick: () -> Unit,
  onForgotPasswordClick: () -> Unit,
  viewModel: LoginViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  LoginContent(
    uiState = uiState,
    onEmailChange = { viewModel.updateEmailField(email = it) },
    onPasswordChange = { viewModel.updatePasswordField(password = it) },
    onSubmitClick = { viewModel.loginUser() },
    onLoginSuccess = onLoginSuccess,
    onRegisterClick = onRegisterClick,
    onForgotPasswordClick = onForgotPasswordClick,
    modifier = modifier
  )
}
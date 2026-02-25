package com.decoutkhanqindev.dexreader.presentation.screens.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.decoutkhanqindev.dexreader.presentation.screens.auth.register.components.RegisterContent

@Composable
fun RegisterScreen(
  onNavigateBack: () -> Unit,
  onRegisterSuccess: () -> Unit,
  viewModel: RegisterViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  RegisterContent(
    uiState = uiState,
    onEmailChange = viewModel::updateEmail,
    onPasswordChange = viewModel::updatePassword,
    onConfirmPasswordChange = viewModel::updateConfirmPassword,
    onNameChange = viewModel::updateName,
    onSubmitClick = viewModel::submit,
    onRegisterSuccess = onRegisterSuccess,
    onNavigateBack = onNavigateBack,
    onRetry = viewModel::retry,
    modifier = modifier
  )
}